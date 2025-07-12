import { useState } from "react";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { deleteQuizById } from "../../../../../services/QuizServices"; // 🔁 Đảm bảo có hàm này

const DeleteQuizModal = ({ show, setShow, onDeleteQuiz, quizData }) => {
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
    if (!(quizData && quizData.id)) return;

    setIsLoading(true);
    try {
      const res = await deleteQuizById(quizData.id);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        showToast("success", "Xóa bài quiz thành công!");
        onDeleteQuiz(); // Refresh quiz list
        handleClose();
      } else {
        showToast("warning", res?.message || "Không thể xóa bài quiz.");
      }
    } catch (err) {
      console.error("Error deleting quiz:", err);
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
        <p>
          Bạn có chắc chắn muốn xóa <strong>bài quiz</strong> này không?
        </p>
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

export default DeleteQuizModal;
