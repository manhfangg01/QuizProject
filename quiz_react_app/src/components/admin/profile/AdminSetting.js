import { useState, useEffect } from "react";
import { Form, Button, Container, Card } from "react-bootstrap";
import { LuImagePlus } from "react-icons/lu";
import { toast } from "react-toastify";
import { getProfile, putUpdateProfile } from "../../../services/UserServices";
import { useNavigate } from "react-router-dom";
import defaultAvatar from "../../../assets/banner.webp";
const AdminSetting = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    id: "",
    fullName: "",
    about: "",
    email: "",
  });

  const [previewAvatar, setPreviewAvatar] = useState("");
  const [avatar, setAvatar] = useState("");

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({ ...prev, [name]: value }));
  };

  const handleUploadImage = (event) => {
    if (event.target?.files?.[0]) {
      const file = event.target.files[0];
      setPreviewAvatar(URL.createObjectURL(file));
      setAvatar(file);
    }
  };

  const fetchProfile = async () => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      const res = await getProfile(userInfo.id);
      if (res.statusCode === 200) {
        setFormData({
          id: userInfo.id || "",
          fullName: res.data.username || "",
          about: res.data?.about || "",
          email: res.data.email || "",
        });
        setPreviewAvatar(res.data.avatar || "");
      }
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const res = await putUpdateProfile(formData.id, formData.fullName, formData.about, avatar);
      if (res.statusCode === 200) {
        // Cập nhật localStorage
        const stored = localStorage.getItem("user");
        let userObj = stored ? JSON.parse(stored) : {};

        userObj.fullName = formData.fullName;
        userObj.about = formData.about;
        if (res.data?.avatar) {
          userObj.avatar = res.data.avatar;
        }

        localStorage.setItem("user", JSON.stringify(userObj));
        toast.success("Cập nhật thành công!");
        navigate("/admins/profile");
      } else {
        toast.error("Cập nhật thất bại!");
      }
    } catch (err) {
      console.log("update error", err);
      toast.error("Có lỗi xảy ra!");
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  return (
    <Container className="pt-4">
      <Card className="p-4 shadow-sm rounded-4">
        <h4 className="mb-4 text-primary">Cập nhật thông tin Admin</h4>

        <p>
          Email: <strong>{formData.email}</strong> <span style={{ fontStyle: "italic", color: "gray" }}>(Không thể thay đổi email quản trị)</span>
        </p>

        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Họ và tên</Form.Label>
            <Form.Control type="text" name="fullName" value={formData.fullName} onChange={handleChange} placeholder="Nhập tên hiển thị" />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Mô tả ngắn</Form.Label>
            <Form.Control as="textarea" name="about" rows={3} placeholder="Mô tả về quản trị viên" value={formData.about} onChange={handleChange} />
          </Form.Group>

          <Form.Group className="mb-4">
            <Form.Label>Ảnh đại diện</Form.Label>
            <div className="mb-2">
              <label className="btn btn-outline-secondary" htmlFor="avatarUpload">
                <LuImagePlus className="me-2" />
                Tải ảnh
              </label>
              <input type="file" id="avatarUpload" className="d-none" accept="image/*" onChange={handleUploadImage} />
            </div>
            <div>
              {previewAvatar ? (
                <img src={previewAvatar} alt="Preview" className="img-thumbnail" style={{ maxWidth: "180px", maxHeight: "180px" }} />
              ) : (
                <img src={defaultAvatar} alt="default" className="img-thumbnail" style={{ maxWidth: "180px" }} />
              )}
            </div>
          </Form.Group>

          <Button type="submit" variant="primary">
            Lưu thay đổi
          </Button>
        </Form>
      </Card>
    </Container>
  );
};

export default AdminSetting;
