import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { LuImagePlus } from "react-icons/lu";
import { Bounce, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { putUpdateUser } from "../../../../../services/UserServices"; // Giả sử API update

const UpdateUserModal = ({ show, setShow, onUpdateUser, userData }) => {
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("USER");
  const [previewAvatar, setPreviewAvatar] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [imageFile, setImageFile] = useState(null);
  const [email, setEmail] = useState("");

  // Gán dữ liệu khi mở modal
  useEffect(() => {
    if (userData) {
      setFullName(userData.fullName || "");
      setEmail(userData.email || "");
      setRole(userData.role || "USER");
      setPreviewAvatar(userData.userAvatarUrls || "");
    }
  }, [userData]); // Hàm này sẽ chạy lại mỗi khi userData có tahy đổi

  const handleClose = () => {
    setShow(false);
    setFullName("");
    setImageFile(null);
    setIsLoading(false);
    setPreviewAvatar("");
    setRole("USER");
    setEmail("");
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
      const res = await putUpdateUser(
        userData.id, // ID phải có để backend biết đang update ai
        fullName,
        role,
        imageFile // hoặc gửi file tùy backend xử lý
      );
      if (res && (res.statusCode === 200 || res.statusCode === 201)) {
        showToast("success", "Cập nhật người dùng thành công!");
        onUpdateUser(); // gọi lại danh sách người dùng
        handleClose();
      } else {
        showToast("warning", "Cập nhật không thành công!");
      }
    } catch (err) {
      console.error("Error updating user:", err);
      showToast("error", err.response?.data?.message || "Đã xảy ra lỗi!");
    } finally {
      setIsLoading(false);
    }
  };

  const handleUploadImage = (event) => {
    if (event.target?.files?.[0]) {
      const file = event.target.files[0];
      setPreviewAvatar(URL.createObjectURL(file));
      setImageFile(file);
    }
  };

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Cập nhật người dùng</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <form className="row g-3">
          <div className="col-md-6">
            <label className="form-label">Email</label>
            <input type="email" className="form-control" value={email} disabled />
          </div>

          <div className="col-md-6">
            <label className="form-label">Họ và tên</label>
            <input type="text" className="form-control" placeholder="Nhập họ và tên" value={fullName} onChange={(e) => setFullName(e.target.value)} required />
          </div>

          <div className="col-md-6">
            <label className="form-label">Vai trò</label>
            <select className="form-select" value={role} onChange={(e) => setRole(e.target.value)}>
              <option value="USER">Người dùng</option>
              <option value="ADMIN">Quản trị viên</option>
            </select>
          </div>

          <div className="col-12">
            <label className="btn btn-outline-primary" htmlFor="avatarUpload">
              <LuImagePlus className="me-2" />
              Tải lên ảnh đại diện
            </label>
            <input type="file" id="avatarUpload" className="d-none" accept="image/*" onChange={handleUploadImage} />
          </div>

          <div className="col-12">
            {previewAvatar ? (
              <img src={previewAvatar} alt="Preview" className="img-thumbnail mt-2" style={{ maxWidth: "200px", maxHeight: "200px" }} />
            ) : (
              <div className="text-muted mt-2">Chưa có ảnh đại diện</div>
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

export default UpdateUserModal;
