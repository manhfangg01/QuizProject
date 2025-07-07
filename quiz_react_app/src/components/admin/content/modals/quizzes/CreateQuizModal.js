import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { postCreateNewQuiz } from "../../../../../services/QuizServices";

const CreateQuizModal = ({ show, setShow, onCreateQuiz }) => {
  const [title, setTitle] = useState("");
  const [subjectName, setSubjectName] = useState("");
  const [timeLimit, setTimeLimit] = useState(15);
  const [difficulty, setDifficulty] = useState("EASY");
  const [isActive, setIsActive] = useState(true);
  const [questions, setQuestions] = useState("");
  const [isLoading, setIsLoading] = useState(false);

  const showToast = (type, message) => {
    toast[type](message, {
      position: "top-center",
      autoClose: 5000,
      transition: Bounce,
      theme: "light",
    });
  };

  const handleClose = () => {
    setShow(false);
    setTitle("");
    setSubjectName("");
    setTimeLimit(15);
    setDifficulty("EASY");
    setIsActive(true);
    setQuestions("");
    setIsLoading(false);
  };

  const handleSubmit = async () => {
    if (!title || !subjectName || !timeLimit || !difficulty || !questions) {
      showToast("warning", "Vui lòng nhập đầy đủ thông tin!");
      return;
    }

    const questionIds = questions
      .split(",")
      .map((id) => id.trim())
      .filter((id) => id !== "")
      .map((id) => parseInt(id));

    if (questionIds.length === 0 || questionIds.some(isNaN)) {
      showToast("error", "Danh sách ID câu hỏi không hợp lệ!");
      return;
    }

    try {
      setIsLoading(true);
      const res = await postCreateNewQuiz({
        title,
        subjectName,
        timeLimit,
        difficulty,
        isActive,
        questions: questionIds,
      });

      if (res.statusCode === 200 || res.statusCode === 201) {
        showToast("success", "Tạo bài quiz thành công!");
        onCreateQuiz();
        handleClose();
      } else {
        showToast("warning", res.message || "Tạo quiz thất bại!");
      }
    } catch (err) {
      console.error("Error creating quiz:", err);
      const msg = err?.response?.data?.message || "Đã xảy ra lỗi khi tạo quiz!";
      showToast("error", msg);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Tạo bài Quiz mới</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Tiêu đề</label>
              <input type="text" className="form-control" value={title} onChange={(e) => setTitle(e.target.value)} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Môn học</label>
              <input type="text" className="form-control" value={subjectName} onChange={(e) => setSubjectName(e.target.value)} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Thời gian (phút)</label>
              <input type="number" className="form-control" value={timeLimit} onChange={(e) => setTimeLimit(parseInt(e.target.value))} min={1} />
            </div>
            <div className="col-md-6">
              <label className="form-label">Độ khó</label>
              <select className="form-select" value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
                <option value="EASY">Dễ</option>
                <option value="MEDIUM">Trung bình</option>
                <option value="HARD">Khó</option>
              </select>
            </div>
            <div className="col-md-6">
              <label className="form-label">Hoạt động</label>
              <select className="form-select" value={isActive} onChange={(e) => setIsActive(e.target.value === "true")}>
                <option value="true">Đang hoạt động</option>
                <option value="false">Không hoạt động</option>
              </select>
            </div>
            <div className="col-md-12">
              <label className="form-label">ID các câu hỏi (phân cách bằng dấu phẩy)</label>
              <input type="text" className="form-control" placeholder="VD: 101, 102, 103" value={questions} onChange={(e) => setQuestions(e.target.value)} />
            </div>
          </form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Đóng
          </Button>
          <Button variant="primary" onClick={handleSubmit} disabled={isLoading}>
            {isLoading ? "Đang xử lý..." : "Lưu"}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default CreateQuizModal;
