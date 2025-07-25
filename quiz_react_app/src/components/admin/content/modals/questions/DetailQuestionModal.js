import { useState } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const DetailQuestionModal = ({ show, setShow, questionData }) => {
  const [showImageModal, setShowImageModal] = useState(false);
  const [selectedImage, setSelectedImage] = useState("");

  const handleClose = () => {
    setShow(false);
  };

  const handleImageClick = (imageUrl) => {
    setSelectedImage(imageUrl);
    setShowImageModal(true);
  };

  return (
    <>
      {/* Modal chính hiển thị chi tiết câu hỏi */}
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
              {questionData?.options?.length > 0 ? (
                <ul>
                  {questionData.options.map((opt) => (
                    <li key={opt.id}>Option ID: {opt.id}</li>
                  ))}
                </ul>
              ) : (
                <div className="text-muted">Không có lựa chọn nào.</div>
              )}
              <hr />

              {questionData?.questionImage ? (
                <div className="mt-3">
                  <h5>Ảnh minh họa:</h5>
                  <div className="text-center mt-2">
                    <img
                      src={questionData.questionImage}
                      alt={`Ảnh minh họa câu hỏi ${questionData.id}`}
                      className="img-fluid rounded border cursor-pointer"
                      style={{
                        maxHeight: "300px",
                        maxWidth: "100%",
                        objectFit: "contain",
                        cursor: "pointer",
                      }}
                      onClick={() => handleImageClick(questionData.questionImage)}
                    />
                    <div className="mt-2 text-muted">
                      <small>Click vào ảnh để xem kích thước đầy đủ</small>
                    </div>
                  </div>
                </div>
              ) : (
                <div className="text-muted">Không có ảnh minh họa.</div>
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

      {/* Modal phóng to ảnh */}
      <Modal show={showImageModal} onHide={() => setShowImageModal(false)} centered size="lg">
        <Modal.Header closeButton>
          <Modal.Title>Ảnh minh họa phóng to</Modal.Title>
        </Modal.Header>
        <Modal.Body className="text-center">
          <img src={selectedImage} alt="Ảnh phóng to" className="img-fluid" style={{ maxHeight: "70vh" }} />
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowImageModal(false)}>
            Đóng
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default DetailQuestionModal;
