import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { LuImagePlus } from "react-icons/lu";
import axios from "axios";
import { Toast } from "react-bootstrap";

function CreateUserModal() {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("USER");
  const [previewAvatar, setPreviewAvatar] = useState("");

  const [imageFile, setImageFile] = useState(null); // file thực

  // Trong component
  const handleSubmit = async () => {
    try {
      const formData = new FormData();

      const user = {
        email,
        password,
        fullName,
        role,
      };

      const userBlob = new Blob([JSON.stringify(user)], {
        type: "application/json",
      });

      formData.append("createUserRequest", userBlob);

      if (imageFile) {
        formData.append("userAvatar", imageFile);
      }

      const token = localStorage.getItem("accessToken");
      if (!token) {
        alert("Chưa đăng nhập. Vui lòng đăng nhập trước khi tạo user.");
        return;
      }

      const res = await axios.post("http://localhost:8080/api/admin/users/create", formData, {
        headers: {
          // KHÔNG set Content-Type thủ công để axios tự đặt boundary
          Authorization: `Bearer ${token}`,
        },
      });

      // Kiểm tra phản hồi thành công
      if (res.status === 200 && res.data.statusCode === 200) {
        console.log("✅ Tạo user thành công");
        handleClose(); // đóng modal nếu có
      } else {
        console.warn("⚠️ Tạo user không thành công:", res.data);
      }
    } catch (err) {
      if (err.response?.status === 403) {
        alert("❌ Bạn không có quyền thực hiện thao tác này.");
      } else {
        alert("Đã có lỗi xảy ra khi tạo user.");
      }
      console.error("Lỗi tạo user:", err);
    }
  };

  const handleUploadImage = (event) => {
    if (event.target && event.target.files && event.target.files[0]) {
      const file = event.target.files[0];
      setPreviewAvatar(URL.createObjectURL(file)); // chỉ để hiển thị
      setImageFile(file); // để gửi lên server
    }
  };

  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        Launch demo modal
      </Button>

      <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Add new user</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Email</label>
              <input
                type="email"
                className="form-control"
                value={email}
                onChange={(event) => {
                  setEmail(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Password</label>
              <input
                type="password"
                className="form-control"
                value={password}
                onChange={(event) => {
                  setPassword(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Fullname</label>
              <input
                type="text"
                className="form-control"
                placeholder="User's fullname"
                value={fullName}
                onChange={(event) => {
                  setFullName(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Role</label>
              <select
                className="form-select"
                onChange={(event) => {
                  setRole(event.target.value);
                }}
              >
                <option selected value={"USER"}>
                  USER
                </option>
                <option value={"ADMIN"}>ADMIN</option>
              </select>
            </div>
            <div className="col-12 label-uploading ">
              <label className="form-label btn " htmlFor="lableUploading">
                <LuImagePlus /> Avatar
              </label>
              <input type="file" className="form-control" id="lableUploading" hidden onChange={(event) => handleUploadImage(event)} />
            </div>

            <div className="col-md-12 modal-add-user">{!previewAvatar ? <span>Preview Image</span> : <img src={previewAvatar} alt="User avatar preview" className="img-preview"></img>}</div>
          </form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
export default CreateUserModal;
