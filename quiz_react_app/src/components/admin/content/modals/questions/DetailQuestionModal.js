import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const DetailQuestionModal = ({ show, setShow, questionData }) => {
  const handleClose = () => {
    setShow(false);
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết câu hỏi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {questionData ? (
          <>
            <div className="mb-3">
              <strong>ID:</strong> {questionData.id}
            </div>
            <div className="mb-3">
              <strong>Nội dung:</strong> {questionData.context}
            </div>

            <hr />
            <h5>Danh sách lựa chọn:</h5>
            {questionData && questionData.options.length > 0 ? (
              <ul>
                {questionData.options.map((opt) => (
                  <li key={opt.id}>Option ID: {opt.id}</li>
                ))}
              </ul>
            ) : (
              <div className="text-muted">Không có lựa chọn nào.</div>
            )}
          </>
        ) : (
          <div className="text-muted">Không có dữ liệu câu hỏi.</div>
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

export default DetailQuestionModal;
