import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { postCreateQuiz } from "../../../../../services/QuizServices";
import { Badge, Form } from "react-bootstrap";

const CreateQuizModal = ({ show, setShow, onCreateQuiz, setShowQuestionSelection, setQuestions, fetchAllQuestions, selectedQuestionIds, setSelectedQuestionIds }) => {
  const [title, setTitle] = useState("");
  const [subjectName, setSubjectName] = useState("");
  const [timeLimit, setTimeLimit] = useState(15);
  const [difficulty, setDifficulty] = useState("EASY");
  const [isActive, setIsActive] = useState(true);
  const [isLoading, setIsLoading] = useState(false);
  const [audioFile, setAudioFile] = useState(null);
  const handleClose = () => {
    setShow(false);
    setTitle("");
    setSubjectName("");
    setTimeLimit(15);
    setDifficulty("EASY");
    setIsActive(true);
    setQuestions([]);
    setIsLoading(false);
    setSelectedQuestionIds([]);
  };

  useEffect(() => {
    fetchAllQuestions(1, {});
  }, []);
  const handleSubmit = async () => {
    if (!title || !subjectName || !timeLimit || !difficulty) {
      toast.warning("Vui lòng nhập đầy đủ thông tin!");
      return;
    }

    if (selectedQuestionIds.length < 2) {
      toast.warning("Vui lòng chọn từ 2 câu hỏi trở lên");
      return;
    }

    // ✅ Kiểm tra định dạng file audio nếu có
    if (audioFile) {
      const allowedTypes = ["audio/mpeg", "audio/wav", "audio/ogg"];
      if (!allowedTypes.includes(audioFile.type)) {
        toast.warning("Định dạng audio không hợp lệ! Chỉ chấp nhận .mp3, .wav hoặc .ogg");
        return;
      }
    }

    try {
      setIsLoading(true);
      const res = await postCreateQuiz(
        title,
        subjectName,
        timeLimit,
        difficulty,
        isActive,
        selectedQuestionIds,
        audioFile // ✅ Gửi file audio lên backend
      );

      if (res.statusCode === 200 || res.statusCode === 201) {
        toast.success("Tạo bài quiz thành công!");
        onCreateQuiz();
        handleClose();
      } else {
        toast.warning(res.message || "Tạo quiz thất bại!");
      }
    } catch (err) {
      console.error("Error creating quiz:", err);
      const msg = err?.response?.data?.message || "Đã xảy ra lỗi khi tạo quiz!";
      toast.warning(msg);
    } finally {
      setIsLoading(false);
    }
  };
  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Tạo bài Quiz mới</Modal.Title>
      </Modal.Header>

      <Modal.Body>
        {/* Thông báo lỗi nếu có */}
        {/* Bạn có thể dùng state error nếu muốn */}

        {/* Nhập thông tin Quiz */}
        <Form.Group className="mb-3">
          <Form.Label>Tiêu đề bài Quiz</Form.Label>
          <Form.Control type="text" value={title} onChange={(e) => setTitle(e.target.value)} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Môn học</Form.Label>
          <Form.Control type="text" placeholder="Ví dụ: Toán, Lý, Hóa..." value={subjectName} onChange={(e) => setSubjectName(e.target.value)} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Thời gian làm bài (phút)</Form.Label>
          <Form.Control type="number" value={timeLimit} onChange={(e) => setTimeLimit(parseInt(e.target.value))} min={1} />
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
          <Form.Check type="checkbox" label="Kích hoạt bài Quiz ngay" checked={isActive} onChange={(e) => setIsActive(e.target.checked)} />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Audio</Form.Label>
          <Form.Control type="file" onChange={(e) => setAudioFile(e.target.files[0])} />
        </Form.Group>

        {/* Chọn câu hỏi */}
        <Form.Group className="mb-3">
          <Form.Label>Danh sách câu hỏi đã chọn ({selectedQuestionIds.length})</Form.Label>
          <div className="mb-2">
            {selectedQuestionIds.length > 0 ? (
              selectedQuestionIds.map((id) => (
                <Badge key={id} bg="secondary" className="me-2">
                  {id}
                </Badge>
              ))
            ) : (
              <div className="text-muted">Chưa chọn câu hỏi nào</div>
            )}
          </div>
          <div className="d-flex gap-2">
            <Button
              variant="primary"
              onClick={() => {
                fetchAllQuestions(1, {}); // load câu hỏi từ trang 1
                setShowQuestionSelection(true); // mở modal chọn câu hỏi
              }}
            >
              {selectedQuestionIds.length > 0 ? "Thay đổi câu hỏi" : "+ Chọn câu hỏi"}
            </Button>
            {selectedQuestionIds.length > 0 && (
              <Button variant="outline-danger" onClick={() => setSelectedQuestionIds([])}>
                Xóa hết
              </Button>
            )}
          </div>
        </Form.Group>
      </Modal.Body>

      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit} disabled={isLoading}>
          {isLoading ? "Đang tạo..." : "Tạo bài Quiz"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CreateQuizModal;
