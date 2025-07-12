import { useState } from "react";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { deleteResultById } from "../../../../../services/ResultServices"; // Thay bằng service xóa result

const DeleteResultModal = ({ show, setShow, onDeleteResult, resultData }) => {
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
    if (!(resultData && resultData.id)) return;

    setIsLoading(true);
    try {
      const res = await deleteResultById(resultData.id); // Sử dụng hàm xóa result
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        showToast("success", "Xóa kết quả thành công!");
        onDeleteResult(); // Refresh result list
        handleClose();
      } else {
        showToast("warning", res?.message || "Không thể xóa kết quả.");
      }
    } catch (err) {
      console.error("Error deleting result:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi khi xóa kết quả!");
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="md">
      <Modal.Header closeButton>
        <Modal.Title>Xác nhận xóa kết quả</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <p>
          Bạn có chắc chắn muốn xóa <strong>kết quả #{resultData?.id}</strong> của <strong>User #{resultData?.userId}</strong> không?
        </p>
        <p className="text-muted">Hành động này không thể hoàn tác.</p>
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="danger" onClick={handleDelete} disabled={isLoading}>
          {isLoading ? "Đang xóa..." : "Xác nhận xóa"}
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DeleteResultModal;
