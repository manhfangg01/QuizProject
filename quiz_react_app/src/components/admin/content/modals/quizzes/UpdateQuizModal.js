import { useEffect, useState } from "react";
import { Button, Modal, Form, Badge } from "react-bootstrap";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { putUpdateQuiz } from "../../../../../services/QuizServices"; // 🔁 Đổi sang API cập nhật quiz

const UpdateQuizModal = ({ show, setShow, onUpdateQuiz, quizData, setShowUpdateQuestions, updatedQuestionIds, setUpdatedQuestionIds, fetchAllQuestions }) => {
  const [title, setTitle] = useState("");
  const [subjectName, setSubjectName] = useState("");
  const [timeLimit, setTimeLimit] = useState(15);
  const [difficulty, setDifficulty] = useState("EASY");
  const [isActive, setIsActive] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const [audioFile, setAudioFile] = useState(null);

  // Load dữ liệu quiz khi mở modal
  useEffect(() => {
    if (quizData) {
      setTitle(quizData.title || "");
      setSubjectName(quizData.subjectName || "");
      setTimeLimit(quizData.timeLimit || 15);
      setDifficulty(quizData.difficulty || "EASY");
      setIsActive(quizData.isActive ?? true);
      const ids = quizData.questions?.map((q) => q.questionId) || [];
      setUpdatedQuestionIds(ids);
    }
    fetchAllQuestions(1, {});
  }, [show]);

  const handleClose = () => {
    setShow(false);
    setIsLoading(false);
    setTitle("");
    setSubjectName("");
    setTimeLimit(15); // ✅ Thay null bằng giá trị mặc định => K nên set bằng null
    setIsActive(true); // ✅ Thay null bằng giá trị mặc định
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (!title || !subjectName || updatedQuestionIds.length < 2) {
      toast.warning("Vui lòng nhập đầy đủ thông tin và chọn ít nhất 2 câu hỏi.");
      return;
    }

    setIsLoading(true);
    try {
      const res = await putUpdateQuiz(
        quizData.quizId,
        title,
        subjectName,
        timeLimit,
        difficulty,
        isActive,
        updatedQuestionIds,
        audioFile // ✅ truyền vào đây
      );
      if (res?.statusCode === 200 || res?.statusCode === 201) {
        toast.success("Cập nhật bài Quiz thành công");
        onUpdateQuiz(1, {});
        handleClose();
      } else {
        toast.warning("Cập nhật không thành công!");
      }
    } catch (err) {
      if (err.response) {
        toast.warning("API trả về lỗi: " + err.response.data.message);
      } else {
        console.log("👉 Response lỗi có body:", err);
      }
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật bài Quiz</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        <Form.Group className="mb-3">
          <Form.Label>Tiêu đề</Form.Label>
          <Form.Control value={title} onChange={(e) => setTitle(e.target.value)} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Môn học</Form.Label>
          <Form.Control value={subjectName} onChange={(e) => setSubjectName(e.target.value)} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Thời gian làm bài (phút)</Form.Label>
          <Form.Control type="number" min={1} value={timeLimit} onChange={(e) => setTimeLimit(Number(e.target.value))} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Độ khó</Form.Label>
          <Form.Select value={difficulty} onChange={(e) => setDifficulty(e.target.value)}>
            <option value="EASY">Dễ</option>
            <option value="MEDIUM">Trung bình</option>
            <option value="HARD">Khó</option>
          </Form.Select>
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Check type="checkbox" label="Kích hoạt bài quiz" checked={isActive} onChange={(e) => setIsActive(e.target.checked)} />
        </Form.Group>
        <Form.Group className="mb-3">
          <Form.Label>Audio mô tả (nếu có)</Form.Label>
          <Form.Control type="file" accept="audio/*" onChange={(e) => setAudioFile(e.target.files[0])} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Câu hỏi đã chọn ({updatedQuestionIds.length})</Form.Label>
          <div>
            {updatedQuestionIds.length > 0 ? (
              updatedQuestionIds.map((id) => (
                <Badge bg="secondary" key={id} className="me-2">
                  {id}
                </Badge>
              ))
            ) : (
              <div className="text-muted">Chưa chọn câu hỏi nào.</div>
            )}
          </div>
          {updatedQuestionIds.length > 0 && (
            <Button className="btn-danger mt-2" onClick={() => setUpdatedQuestionIds([])}>
              Xóa hết câu hỏi
            </Button>
          )}
        </Form.Group>

        <Button variant="outline-primary" onClick={() => setShowUpdateQuestions(true)}>
          + Chọn câu hỏi
        </Button>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit} disabled={isLoading}>
          {isLoading ? "Đang cập nhật..." : "Lưu thay đổi"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateQuizModal;
