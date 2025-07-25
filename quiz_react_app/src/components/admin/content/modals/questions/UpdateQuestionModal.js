import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { putUpdateQuestion } from "../../../../../services/QuestionServices";
import { Badge, Form } from "react-bootstrap";

const UpdateQuestionModal = ({ show, setShow, onUpdateQuestion, questionData, setShowUpdateOptions, selectedOptionIds, setSelectedOptionIds }) => {
  const [context, setContext] = useState("");
  const [selectedIds, setSelectedIds] = useState([]);
  const [isLoading, setIsLoading] = useState(false);
  const [questionImage, setQuestionImage] = useState(null);
  const [previewImage, setPreviewImage] = useState("");

  useEffect(() => {
    if (questionData) {
      setContext(questionData.context || "");
      setPreviewImage(questionData.questionImage || "");
      const ids = questionData.options?.map((opt) => opt.id) || [];
      setSelectedOptionIds(ids);
    }
  }, [show, questionData]);

  useEffect(() => {
    setSelectedIds(selectedOptionIds);
  }, [selectedOptionIds]);

  const handleClose = () => {
    setContext("");
    setSelectedIds([]);
    setQuestionImage(null);
    setPreviewImage("");
    setShow(false);
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setQuestionImage(file);
      setPreviewImage(URL.createObjectURL(file));
    }
  };

  const handleRemoveImage = () => {
    setQuestionImage(null);
    setPreviewImage("");
  };

  const handleSubmit = async () => {
    if (!context) {
      toast.warning("Vui lòng nhập nội dung câu hỏi");
      return;
    }

    setIsLoading(true);
    try {
      const res = await putUpdateQuestion(questionData.questionId, context, selectedOptionIds, questionImage);

      if (res?.statusCode === 200 || res?.statusCode === 201) {
        toast.success("Cập nhật câu hỏi thành công");
        onUpdateQuestion();
        handleClose();
      } else {
        toast.warning("Cập nhật không thành công!");
      }
    } catch (err) {
      toast.error("Đã xảy ra lỗi khi cập nhật câu hỏi!");
      console.error(err);
    } finally {
      setIsLoading(false);
    }
  };

  const handleShowUpdateOptionModal = () => {
    setShowUpdateOptions(true);
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật câu hỏi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group className="mb-3">
          <Form.Label>Nội dung câu hỏi</Form.Label>
          <Form.Control as="textarea" rows={3} value={context} onChange={(e) => setContext(e.target.value)} placeholder="Nhập nội dung câu hỏi..." />
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Ảnh minh họa</Form.Label>
          <Form.Control type="file" accept="image/*" onChange={handleImageChange} className="mb-2" />
          {previewImage && (
            <div className="mt-2">
              <img src={previewImage} alt="Ảnh minh họa" className="img-thumbnail" style={{ maxHeight: "200px" }} />
              <Button variant="danger" size="sm" className="mt-2" onClick={handleRemoveImage}>
                Xóa ảnh
              </Button>
            </div>
          )}
        </Form.Group>

        <Form.Group className="mb-3">
          <Form.Label>Lựa chọn đã chọn ({selectedOptionIds.length}/4):</Form.Label>
          <div>
            {selectedOptionIds.length > 0 ? (
              selectedOptionIds.map((id) => (
                <Badge bg="secondary" key={id} className="me-2">
                  {id}
                </Badge>
              ))
            ) : (
              <div className="text-muted">Chưa chọn lựa chọn nào.</div>
            )}
          </div>
          {selectedOptionIds.length > 0 && (
            <Button className="btn-danger mt-2" onClick={() => setSelectedOptionIds([])}>
              Xóa hết các lựa chọn
            </Button>
          )}
        </Form.Group>

        <Button variant="outline-primary" onClick={handleShowUpdateOptionModal}>
          + Chọn Options
        </Button>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit} disabled={isLoading}>
          {isLoading ? "Đang xử lý..." : "Lưu thay đổi"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateQuestionModal;
