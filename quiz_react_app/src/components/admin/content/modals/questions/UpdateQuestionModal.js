import { useEffect, useState } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { Bounce, toast } from "react-toastify";
import { putUpdateQuestion } from "../../../../../services/QuestionServices";
import "react-toastify/dist/ReactToastify.css";

const UpdateQuestionModal = ({ show, setShow, onUpdateQuestion, questionData, selectedOptionIds, setSelectedOptionIds, availableOptions, setShowOptionSelection }) => {
  const [context, setContext] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [tempSelectedOptionIds, setTempSelectedOptionIds] = useState([]);

  const handleClose = () => {
    setShow(false);
    setContext("");
    setIsLoading(false);
    setShowOptionSelection(false);
    setTempSelectedOptionIds([]);
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

  const handleCheckboxChange = (id) => {
    if (tempSelectedOptionIds.includes(id)) {
      setTempSelectedOptionIds(tempSelectedOptionIds.filter((optId) => optId !== id));
    } else {
      if (tempSelectedOptionIds.length >= 4) {
        showToast("warning", "Chỉ được chọn tối đa 4 lựa chọn.");
        return;
      }
      setTempSelectedOptionIds([...tempSelectedOptionIds, id]);
    }
  };

  const handleSubmit = async () => {
    if (tempSelectedOptionIds.length < 2 || tempSelectedOptionIds.length > 4) {
      showToast("warning", "Cần chọn từ 2 đến 4 lựa chọn.");
      return;
    }

    setIsLoading(true);
    try {
      const payload = {
        questionId: questionData.id,
        context,
        optionIds: tempSelectedOptionIds,
      };

      const res = await putUpdateQuestion(payload.questionId, payload.context, payload.optionIds);
      if (res.statusCode === 200 || res.statusCode === 201) {
        showToast("success", "Cập nhật câu hỏi thành công!");
        setSelectedOptionIds([...tempSelectedOptionIds]); // cập nhật lại selected chính thức
        onUpdateQuestion();
        handleClose();
      } else {
        showToast("warning", res?.message || "Cập nhật không thành công!");
      }
    } catch (err) {
      console.error("Lỗi cập nhật câu hỏi:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi khi cập nhật!");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    if (show && questionData) {
      setContext(questionData.context || "");
      setTempSelectedOptionIds([...selectedOptionIds]); // clone từ selected ban đầu
    }
  }, [show, questionData]);

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật câu hỏi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form className="row g-3">
          <div className="col-md-12">
            <label className="form-label">Nội dung câu hỏi</label>
            <input type="text" className="form-control" placeholder="Nhập nội dung câu hỏi" value={context} onChange={(e) => setContext(e.target.value)} required />
          </div>

          <div className="col-md-12">
            <label className="form-label">Chọn từ 2 đến 4 lựa chọn</label>
            {availableOptions.length > 0 ? (
              availableOptions.map((opt) => (
                <div className="form-check" key={opt.id}>
                  <input className="form-check-input" type="checkbox" id={`option-${opt.id}`} checked={tempSelectedOptionIds.includes(opt.id)} onChange={() => handleCheckboxChange(opt.id)} />
                  <label className="form-check-label" htmlFor={`option-${opt.id}`}>
                    {opt.context} {opt.isCorrect ? "(Đúng)" : "(Sai)"}
                  </label>
                </div>
              ))
            ) : (
              <p className="text-muted">Không có lựa chọn nào.</p>
            )}
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

export default UpdateQuestionModal;
