import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { LuImagePlus } from "react-icons/lu";
import { Bounce, toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { postCreateNewUser } from "../../../../../services/UserServices";

const CreateUserModal = (props) => {
  const [emailError, setEmailError] = useState("");
  const { show, setShow } = props;
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("USER");
  const [isLoading, setIsLoading] = useState(false);
  const [previewAvatar, setPreviewAvatar] = useState("");
  const [imageFile, setImageFile] = useState(null);
  const onCreateUser = props.onCreateUser;

  const handleClose = () => {
    props.setShow(false);
    setEmail("");
    setEmailError("");
    setFullName("");
    setImageFile(null);
    setIsLoading(false);
    setPassword("");
    setPreviewAvatar("");
    setRole("USER");
  };

  const validateEmail = (email) => {
    return String(email)
      .toLowerCase()
      .match(/^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|.(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/);
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
    setEmailError("");
    setIsLoading(true);

    // Validate input (không dùng throw new Error)
    if (!validateEmail(email)) {
      setEmailError("Email không hợp lệ!");
      showToast("error", "Email không hợp lệ!");
      setIsLoading(false);
      return;
    }

    if (!password || password.length < 6) {
      showToast("error", "Mật khẩu phải có ít nhất 6 ký tự!");
      setIsLoading(false);
      return;
    }

    try {
      // Call API
      const res = await postCreateNewUser(email, password, fullName, role, imageFile);

      // Handle response
      if (res.statusCode === 200 || res.statusCode === 201) {
        showToast("success", "Tạo người dùng thành công!");
        onCreateUser();
        handleClose(); // Đã có reset form trong handleClose
      } else {
        showToast("warning", "Tạo người dùng không thành công!");
      }
    } catch (err) {
      console.error("Error creating user:", err);

      if (err.response?.status === 403) {
        showToast("error", "Bạn không có quyền thực hiện thao tác này!");
      } else if (err.response?.data?.message) {
        showToast("error", err.response.data.message);
      } else {
        showToast("error", "Đã có lỗi xảy ra khi tạo người dùng!");
      }
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
    <>
      <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Thêm người dùng mới</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Email</label>
              <input type="email" className="form-control" value={email} onChange={(e) => setEmail(e.target.value)} required />
              {emailError && (
                <div className="alert alert-danger py-2" role="alert">
                  {emailError}
                </div>
              )}
            </div>
            <div className="col-md-6">
              <label className="form-label">Mật khẩu</label>
              <input type="password" className="form-control" value={password} onChange={(e) => setPassword(e.target.value)} minLength={6} required />
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
            {isLoading ? "Đang xử lý..." : "Lưu"}
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
};

export default CreateUserModal;
