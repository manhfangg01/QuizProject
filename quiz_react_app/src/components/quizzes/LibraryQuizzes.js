import React, { useEffect, useState } from "react";
import { Button, Badge } from "react-bootstrap";
import { getLibraryQuizzes } from "../../services/QuizServices";
import FormLibraryFilter from "./FormLibraryFilter";
import { toast } from "react-toastify";
import CustomPagination from "../admin/content/CustomPagination";
import { CiClock2 } from "react-icons/ci";
import { LiaUserEditSolid } from "react-icons/lia";
import { IoBulbOutline, IoDocumentTextOutline } from "react-icons/io5";
import UserBox from "./UserBox";
import { Link, useNavigate } from "react-router-dom";

const LibraryQuizzes = () => {
  const navigate = useNavigate();
  const [quizzes, setQuizzes] = useState([]);
  const [filter, setFilter] = useState({
    title: "",
    difficulty: "",
    timeLimit: "",
    subject: "",
  });
  const [metadata, setMetadata] = useState({});
  const [avatar, setAvatar] = useState("");
  const [username, setUsername] = useState("");
  const [message, setMessage] = useState("");

  useEffect(() => {
    loadQuizzes(1, {});
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      setAvatar(userInfo.avatar);
      setUsername(userInfo.fullName);
      setMessage("Are you gay ?");
    }
  }, []);

  const loadQuizzes = async (pageNumber, filter) => {
    try {
      const res = await getLibraryQuizzes(pageNumber, filter);
      console.log("check  res library", res);

      if (res.statusCode === 200) {
        const { quizzes, metadata } = res.data;
        setQuizzes(quizzes);
        setMetadata(metadata);
      } else {
        toast.warning("Gọi API thất bại");
      }
    } catch (err) {
      toast.warning(err);
    }
  };
  const onPageChange = (pageNumber, filter) => {
    if (pageNumber === metadata.currentPage) return;
    loadQuizzes(pageNumber, filter);
  };

  const formatTime = (time) => {
    if (!time) return "0";
    const minutes = Math.floor(time % 3600);
    return `${minutes}`;
  };

  const getColorClass = (difficulty) => {
    switch (difficulty) {
      case "EASY":
        return "text-success"; // xanh lá
      case "MEDIUM":
        return "text-warning"; // vàng
      case "HARD":
        return "text-danger"; // đỏ
      default:
        return "text-secondary";
    }
  };

  const handleDoQuiz = ({ quiz }) => {
    const token = localStorage.getItem("accessToken");
    const user = localStorage.getItem("user");

    if (!token || !user) {
      toast.warning("Vui lòng đăng nhập để làm bài thi!");
      navigate("/login");
      return;
    }

    navigate(`/do-quiz/${quiz.quizId}`);
  };

  return (
    <div className="container mt-4">
      <h2>Thư viện đề thi</h2>

      <div className="d-flex justify-content-between">
        <div>
          <FormLibraryFilter filter={filter} setFilter={setFilter} fetchAllQuizzes={loadQuizzes} />
        </div>
        <div>
          <UserBox avatarUrl={avatar} username={username} message={message} />
        </div>
      </div>
      <hr />
      <div className="d-flex flex-wrap gap-3">
        {quizzes.map((quiz) => (
          <div key={quiz.quizId} className="border rounded p-2" style={{ width: "250px" }}>
            <div className="fw-bold text-primary mb-1">{quiz.title}</div>
            <div className="text-muted mb-1">
              <div>
                <CiClock2 /> {formatTime(quiz.timeLimit)} phút | <LiaUserEditSolid /> {quiz.totalParticipants} |{" "}
                <span className={`${getColorClass(quiz.difficulty)}`}>
                  <IoBulbOutline /> {quiz.difficulty}
                </span>
              </div>
              <div>
                <IoDocumentTextOutline /> {quiz.totalQuestions} câu hỏi
              </div>
            </div>
            <div className="mb-2">
              <Badge bg="secondary" className="d-block">
                #{quiz.subject}
              </Badge>
              <Badge bg="info" className="d-block">
                #{quiz.level}
              </Badge>
            </div>
            <div className="d-flex justify-content-center">
              {quiz.resultId ? (
                <Link to={`/results/${quiz.resultId}`}>
                  <Button variant="outline-primary" size="sm">
                    Xem kết quả
                  </Button>
                </Link>
              ) : (
                <Button variant="primary" size="sm" onClick={handleDoQuiz}>
                  Làm bài thi
                </Button>
              )}
            </div>
          </div>
        ))}
      </div>
      <CustomPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
    </div>
  );
};

export default LibraryQuizzes;
