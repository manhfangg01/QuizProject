import { useState } from "react";
import { Modal, Button, Form, Badge } from "react-bootstrap";
import { getOptionsByIds } from "../../../../../services/OptionService";
import { toast } from "react-toastify";

const CreateQuestionModal = ({ show, setShow, onCreateQuestion, setShowOptionSelection, selectedOptionIds = [], setSelectedOptionIds }) => {
  const [context, setContext] = useState("");
  const handleCheckAtLeastOneTrueOption = async (optionIds) => {
    try {
      console.log(optionIds.length);

      if (optionIds.length < 2 || optionIds.length > 4) {
        toast.warning("Số lượng câu hỏi từ 2 đến 4");
        return false;
      }

      const response = await getOptionsByIds(optionIds); // gọi API

      if (response?.statusCode === 200) {
        const listOption = response.data || [];
        const correctCount = listOption.filter((opt) => opt.isCorrect === true).length;

        if (correctCount !== 1) {
          toast.warning("Một câu hỏi cần có đúng 1 đáp án đúng.");
          return false;
        }
        return true;
      } else {
        toast.warning("Không thể lấy dữ liệu lựa chọn từ server.");
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
      toast.warning("Lỗi hệ thống khi kiểm tra đáp án.");
    }

    return false;
  };

  const handleSubmit = async () => {
    if (!context.trim()) {
      toast.warning("Không được bỏ trống nội dung");
      return;
    }

    const isValid = await handleCheckAtLeastOneTrueOption(selectedOptionIds); // sửa selectedIds thành selectedOptionIds
    console.log(selectedOptionIds);
    if (!isValid) {
      return;
    }

    const questionData = {
      context: context.trim(),
      optionIds: selectedOptionIds,
    };

    if (onCreateQuestion(questionData)) {
      handleClose();
    }
    return;
  };

  const handleClose = () => {
    setContext("");
    setShow(false);
    setShowOptionSelection(false);
    setSelectedOptionIds([]);
  };

  const handleShowOptionModal = () => {
    setShowOptionSelection(true);
  };

  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Tạo câu hỏi mới</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form.Group className="mb-3">
          <Form.Label>Nội dung câu hỏi</Form.Label>
          <Form.Control as="textarea" rows={3} value={context} onChange={(e) => setContext(e.target.value)} placeholder="Nhập nội dung câu hỏi..." />
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
            <Button className="btn-danger" onClick={() => setSelectedOptionIds([])}>
              Xóa hết các lựa chọn
            </Button>
          )}
        </Form.Group>

        <Button variant="outline-primary" onClick={handleShowOptionModal}>
          + Chọn Options
        </Button>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit}>
          Tạo câu hỏi
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default CreateQuestionModal;
