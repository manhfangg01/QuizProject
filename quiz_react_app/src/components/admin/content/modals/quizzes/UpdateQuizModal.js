import { useEffect, useState } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { putUpdateQuiz } from "../../../../../services/QuizServices";

const UpdateQuizModal = ({ show, setShow, onUpdateQuiz, quizData }) => {
  const [title, setTitle] = useState("");
  const [subjectName, setSubjectName] = useState("");
  const [timeLimit, setTimeLimit] = useState(1);
  const [isActive, setIsActive] = useState(true);
  const [difficulty, setDifficulty] = useState("EASY");
  const [questionIds, setQuestionIds] = useState([]);

  const [isLoading, setIsLoading] = useState(false);

  useEffect(() => {
    if (quizData) {
      setTitle(quizData.title || "");
      setSubjectName(quizData.subjectName || "");
      setTimeLimit(quizData.timeLimit || 1);
      setIsActive(quizData.isActive ?? true);
      setDifficulty(quizData.difficulty || "EASY");
      setQuestionIds(quizData.questions?.map((q) => q.questionId) || []);
    }
  }, [quizData]);

  const handleClose = () => {
    setShow(false);
    setTitle("");
    setSubjectName("");
    setTimeLimit(1);
    setIsActive(true);
    setDifficulty("EASY");
    setQuestionIds([]);
    setIsLoading(false);
  };

  const showToast = (type, message) => {
    toast[type](message, {
      position: "top-center",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      theme: "light",
      transition: Bounce,
    });
  };

  const handleSubmit = async () => {
    if (!title || !subjectName || timeLimit < 1 || questionIds.length < 1) {
      showToast("error", "Vui lòng điền đầy đủ thông tin hợp lệ!");
      return;
    }

    const requestBody = {
      quizId: quizData.quizId,
      title,
      subjectName,
      timeLimit,
      isActive,
      difficulty,
      questions: questionIds,
    };

    setIsLoading(true);
    try {
      const res = await putUpdateQuiz(requestBody);
      if (res && (res.statusCode === 200 || res.statusCode === 201)) {
        showToast("success", "Cập nhật quiz thành công!");
        onUpdateQuiz();
        handleClose();
      } else {
        showToast("warning", res?.message || "Cập nhật không thành công.");
      }
    } catch (err) {
      console.error("❌ Lỗi cập nhật quiz:", err);
      showToast("error", err.response?.data?.message || "Đã xảy ra lỗi!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="xl">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật bài Quiz</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form className="row g-3">
          <div className="col-md-6">
            <label className="form-label">Tiêu đề</label>
            <input type="text" className="form-control" value={title} onChange={(e) => setTitle(e.target.value)} required />
          </div>

          <div className="col-md-6">
            <label className="form-label">Môn học</label>
            <input type="text" className="form-control" value={subjectName} onChange={(e) => setSubjectName(e.target.value)} required />
          </div>

          <div className="col-md-4">
            <label className="form-label">Thời gian (phút)</label>
            <input type="number" className="form-control" value={timeLimit} onChange={(e) => setTimeLimit(+e.target.value)} min={1} required />
          </div>

          <div className="col-md-4">
            <label className="form-label">Độ khó</label>
            <select className="form-select" value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
              <option value="EASY">Dễ</option>
              <option value="MEDIUM">Trung bình</option>
              <option value="HARD">Khó</option>
            </select>
          </div>

          <div className="col-md-4">
            <label className="form-label">Trạng thái</label>
            <select className="form-select" value={isActive ? "true" : "false"} onChange={(e) => setIsActive(e.target.value === "true")}>
              <option value="true">Đang hoạt động</option>
              <option value="false">Ngừng hoạt động</option>
            </select>
          </div>

          <div className="col-12">
            <label className="form-label">Danh sách câu hỏi (ID)</label>
            <input
              type="text"
              className="form-control"
              placeholder="Nhập các ID cách nhau bởi dấu phẩy (VD: 1,2,3)"
              value={questionIds.join(",")}
              onChange={(e) => {
                const ids = e.target.value
                  .split(",")
                  .map((id) => parseInt(id.trim()))
                  .filter((id) => !isNaN(id));
                setQuestionIds(ids);
              }}
              required
            />
          </div>
        </form>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Đóng
        </Button>
        <Button variant="primary" onClick={handleSubmit} disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : "Lưu thay đổi"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateQuizModal;
