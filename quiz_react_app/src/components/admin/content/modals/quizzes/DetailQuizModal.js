import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { Badge } from "react-bootstrap";

const DetailQuizModal = ({ show, setShow, quizData }) => {
  const handleClose = () => {
    setShow(false);
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="xl">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết bài Quiz</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {quizData ? (
          <div>
            <div className="mb-3">
              <strong>Tiêu đề:</strong> {quizData.title}
            </div>
            <div className="mb-3">
              <strong>Môn học:</strong> {quizData.subjectName}
            </div>
            <div className="mb-3">
              <strong>Thời gian làm bài:</strong> {quizData.timeLimit} phút
            </div>
            <div className="mb-3">
              <strong>Trạng thái:</strong> <Badge bg={quizData.isActive ? "success" : "secondary"}>{quizData.isActive ? "Đang hoạt động" : "Ngừng hoạt động"}</Badge>
            </div>
            <div className="mb-3">
              <strong>Độ khó:</strong> <Badge bg="info">{quizData.difficulty}</Badge>
            </div>
            <div className="mb-3">
              <strong>Số người đã làm:</strong> {quizData.totalParticipants || 0}
            </div>

            <div className="mt-4">
              <h5>Câu hỏi</h5>
              {quizData.questions && quizData.questions.length > 0 ? (
                quizData.questions.map((q, idx) => (
                  <div key={q.questionId} className="mb-4">
                    <div>
                      <strong>Câu {idx + 1}:</strong> {q.context}
                    </div>
                    <ul className="mt-2">
                      {q.options.map((opt) => (
                        <li key={opt.id}>
                          {opt.context}{" "}
                          {opt.isCorrect && (
                            <Badge bg="success" className="ms-2">
                              Đúng
                            </Badge>
                          )}
                        </li>
                      ))}
                    </ul>
                  </div>
                ))
              ) : (
                <p className="text-muted">Không có câu hỏi nào.</p>
              )}
            </div>
          </div>
        ) : (
          <div className="text-muted">Không có dữ liệu quiz.</div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Đóng
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DetailQuizModal;
