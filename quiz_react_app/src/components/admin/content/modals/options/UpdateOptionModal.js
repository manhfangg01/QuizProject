import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { putUpdateOption } from "../../../../../services/OptionService";

const UpdateOptionModal = ({ show, setShow, onUpdateOption, optionData }) => {
  const [context, setContext] = useState("");
  const [isCorrect, setIsCorrect] = useState(false);
  const [isLoading, setIsLoading] = useState(false);

  // Set initial data when modal opens
  useEffect(() => {
    if (optionData) {
      setContext(optionData.context || "");
      setIsCorrect(optionData.isCorrect || false);
    }
  }, [optionData]);

  const handleClose = () => {
    setShow(false);
    setContext("");
    setIsCorrect(false);
    setIsLoading(false);
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

  const handleSubmit = async () => {
    setIsLoading(true);
    try {
      const res = await putUpdateOption(
        optionData.id, // ID to identify which option to update
        context,
        isCorrect
      );

      if (res && (res.statusCode === 200 || res.statusCode === 201)) {
        showToast("success", "Cập nhật lựa chọn thành công!");
        onUpdateOption(); // Refresh the options list
        handleClose();
      } else {
        showToast("warning", "Cập nhật không thành công!");
      }
    } catch (err) {
      console.error("Error updating option:", err);
      showToast("error", err.response?.data?.message || "Đã xảy ra lỗi!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật lựa chọn</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form className="row g-3">
          <div className="col-md-12">
            <label className="form-label">Nội dung lựa chọn</label>
            <input type="text" className="form-control" placeholder="Nhập nội dung lựa chọn" value={context} onChange={(e) => setContext(e.target.value)} required />
          </div>

          <div className="col-md-12">
            <div className="form-check">
              <input className="form-check-input" type="checkbox" id="isCorrectCheck" checked={isCorrect} onChange={(e) => setIsCorrect(e.target.checked)} />
              <label className="form-check-label" htmlFor="isCorrectCheck">
                Là đáp án đúng
              </label>
            </div>
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

export default UpdateOptionModal;
