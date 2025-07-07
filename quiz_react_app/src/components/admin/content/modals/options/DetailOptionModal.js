import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const DetailOptionModal = ({ show, setShow, optionData }) => {
  const handleClose = () => {
    setShow(false);
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="md">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết lựa chọn</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {optionData ? (
          <div>
            <div className="mb-3">
              <strong>ID:</strong> {optionData.id}
            </div>
            <div className="mb-3">
              <strong>Nội dung:</strong> {optionData.context}
            </div>
            <div className="mb-3">
              <strong>Là đáp án đúng:</strong> {optionData.isCorrect ? "Có" : "Không"}
            </div>
            <div className="mb-3">
              <strong>ID Câu hỏi:</strong> {optionData.questionId}
            </div>
          </div>
        ) : (
          <div className="text-muted">Không có dữ liệu lựa chọn.</div>
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

export default DetailOptionModal;
