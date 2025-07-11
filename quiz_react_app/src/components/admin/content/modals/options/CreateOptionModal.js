import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { Bounce, toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { postCreateOption } from "../../../../../services/OptionService";

const CreateOptionModal = (props) => {
  const [contextError, setContextError] = useState("");
  const { show, setShow } = props;
  const [context, setContext] = useState("");
  const [isCorrect, setIsCorrect] = useState(false);
  const [isLoading, setIsLoading] = useState(false);
  const onCreateOption = props.onCreateOption;

  const handleClose = () => {
    props.setShow(false);
    setContext("");
    setContextError("");
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
      progress: undefined,
      theme: "light",
      transition: Bounce,
    });
  };

  const handleSubmit = async () => {
    setContextError("");
    setIsLoading(true);

    // Validate input
    if (!context || context.length < 1) {
      setContextError("Nội dung lựa chọn không được để trống!");
      showToast("error", "Nội dung lựa chọn không được để trống!");
      setIsLoading(false);
      return;
    }

    try {
      const res = await postCreateOption(context, isCorrect);
      if (res.statusCode === 200 || res.statusCode === 201) {
        showToast("success", "Tạo lựa chọn thành công!");
        onCreateOption();
        handleClose();
      } else {
        showToast("warning", "Tạo lựa chọn không thành công!");
      }
    } catch (err) {
      console.error("Error creating option:", err);

      if (err.response?.status === 403) {
        showToast("error", "Bạn không có quyền thực hiện thao tác này!");
      } else if (err.response?.data?.message) {
        showToast("error", err.response.data.message);
      } else {
        showToast("error", "Đã có lỗi xảy ra khi tạo lựa chọn!");
      }
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <>
      <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Thêm lựa chọn mới</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="row g-3">
            <div className="col-md-12">
              <label className="form-label">Nội dung lựa chọn</label>
              <input type="text" className="form-control" placeholder="Nhập nội dung lựa chọn" value={context} onChange={(e) => setContext(e.target.value)} required />
              {contextError && (
                <div className="alert alert-danger py-2" role="alert">
                  {contextError}
                </div>
              )}
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
            {isLoading ? "Đang xử lý..." : "Lưu"}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default CreateOptionModal;
