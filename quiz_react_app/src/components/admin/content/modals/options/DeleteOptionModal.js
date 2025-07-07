import { useState } from "react";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { deleteOption } from "../../../../../services/OptionService";

const DeleteOptionModal = ({ show, setShow, onDeleteOption, optionId }) => {
  const [isLoading, setIsLoading] = useState(false);

  const handleClose = () => {
    setShow(false);
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

  const handleDelete = async () => {
    if (!optionId) return;

    setIsLoading(true);
    try {
      const res = await deleteOption(optionId);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        showToast("success", "Xóa lựa chọn thành công!");
        onDeleteOption(); // Refresh the options list
        handleClose();
      } else {
        showToast("warning", res?.message || "Không thể xóa lựa chọn.");
      }
    } catch (err) {
      console.error("Error deleting option:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi khi xóa!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="md">
      <Modal.Header closeButton>
        <Modal.Title>Xác nhận xóa</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>Bạn có chắc chắn muốn xóa lựa chọn này không?</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="danger" onClick={handleDelete} disabled={isLoading}>
          {isLoading ? "Đang xóa..." : "Xác nhận"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DeleteOptionModal;
