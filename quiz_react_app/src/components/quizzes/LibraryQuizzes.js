import React, { useEffect, useState, useCallback } from "react";
import { Button, Badge, Spinner } from "react-bootstrap";
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
  const [loading, setLoading] = useState(false);

  // Load user info from localStorage
  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      try {
        const userInfo = JSON.parse(stored);
        setAvatar(userInfo.avatar || "");
        setUsername(userInfo.fullName || "");
        setMessage("Are you ready for a challenge?");
      } catch (error) {
        console.error("Failed to parse user data:", error);
      }
    }
  }, []);

  // Memoized load quizzes function
  const loadQuizzes = useCallback(async (pageNumber, filter) => {
    setLoading(true);
    try {
      const res = await getLibraryQuizzes(pageNumber, filter);

      if (res.statusCode === 200) {
        const { quizzes, metadata } = res.data;
        setQuizzes(quizzes);
        setMetadata(metadata);
      } else {
        toast.error(res.message || "Failed to load quizzes");
      }
    } catch (err) {
      toast.error(err.message || "An error occurred while loading quizzes");
      console.error("Error loading quizzes:", err);
    } finally {
      setLoading(false);
    }
  }, []);

  // Initial load and filter change handler
  useEffect(() => {
    loadQuizzes(1, filter);
  }, [filter, loadQuizzes]);

  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    loadQuizzes(pageNumber, filter);
  };

  const formatTime = (time) => {
    if (!time) return "0";
    const minutes = Math.floor(time);
    return `${minutes}`;
  };

  const getColorClass = (difficulty) => {
    switch (difficulty) {
      case "EASY":
        return "text-success";
      case "MEDIUM":
        return "text-warning";
      case "HARD":
        return "text-danger";
      default:
        return "text-secondary";
    }
  };

  const handleMoreDetail = (quiz) => {
    const token = localStorage.getItem("accessToken");
    const user = localStorage.getItem("user");

    if (!token || !user) {
      toast.warning("Please login to see more detail !");
      navigate("/login", { state: { from: `/detailedQuiz/${quiz.quizId}` } });
      return;
    }
    navigate(`/detailedQuiz/${quiz.quizId}`);
  };

  if (loading && quizzes.length === 0) {
    return (
      <div className="d-flex justify-content-center mt-5">
        <Spinner animation="border" variant="primary" />
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <h2 className="mb-4">Quiz Library</h2>

      <div className="d-flex justify-content-between align-items-center mb-4">
        <div className="flex-grow-1 me-3">
          <FormLibraryFilter filter={filter} setFilter={setFilter} />
        </div>
        <div>
          <UserBox avatarUrl={avatar} username={username} message={message} />
        </div>
      </div>

      <hr className="mb-4" />

      {quizzes.length === 0 && !loading ? (
        <div className="text-center py-5">
          <h4>No quizzes found</h4>
          <p>Try adjusting your search filters</p>
        </div>
      ) : (
        <>
          <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 row-cols-xl-4 g-4">
            {quizzes.map((quiz) => (
              <div key={quiz.quizId} className="col">
                <div className="card h-100 shadow-sm">
                  <div className="card-body">
                    <h5 className="card-title text-primary" style={{ textAlign: "center" }}>
                      {quiz.title}
                    </h5>

                    <div className="card-text text-muted mb-3">
                      <div className="d-flex align-items-center mb-1">
                        <CiClock2 className="me-2" />
                        <span>{formatTime(quiz.timeLimit)} minutes</span>
                      </div>
                      <div className="d-flex align-items-center mb-1">
                        <LiaUserEditSolid className="me-2" />
                        <span>{quiz.totalParticipants} participants</span>
                      </div>
                      <div className="d-flex align-items-center mb-1">
                        <IoBulbOutline className={`me-2 ${getColorClass(quiz.difficulty)}`} />
                        <span className={getColorClass(quiz.difficulty)}>{quiz.difficulty}</span>
                      </div>
                      <div className="d-flex align-items-center">
                        <IoDocumentTextOutline className="me-2" />
                        <span>{quiz.totalQuestions} questions</span>
                      </div>
                    </div>

                    <div className="mb-3">
                      <Badge bg="secondary" className="me-1">
                        #{quiz.subject}
                      </Badge>
                      <Badge bg="info">#{quiz.level}</Badge>
                    </div>
                  </div>

                  <div className="card-footer bg-transparent border-top-0">
                    {quiz.resultId ? (
                      <Link to={`/results/${quiz.resultId}`} className="btn btn-outline-primary w-100">
                        View Result
                      </Link>
                    ) : (
                      <Button variant="primary" className="w-100" onClick={() => handleMoreDetail(quiz)}>
                        More Detail
                      </Button>
                    )}
                  </div>
                </div>
              </div>
            ))}
          </div>

          {metadata.totalPages > 1 && (
            <div className="mt-4">
              <CustomPagination metadata={metadata} onPageChange={onPageChange} />
            </div>
          )}
        </>
      )}
    </div>
  );
};

export default LibraryQuizzes;
