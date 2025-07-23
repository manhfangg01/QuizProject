import { useState, useEffect } from "react";
import { Form, Button, Container, Card } from "react-bootstrap";
import { LuImagePlus } from "react-icons/lu";
import { toast } from "react-toastify";
import { getProfile, putUpdateProfile } from "../../services/UserServices";
import { Navigate, useNavigate } from "react-router-dom";

const Setting = () => {
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
          userObj.avatar = res.data.avatar; // ✅ dùng URL từ backend
        }

        localStorage.setItem("user", JSON.stringify(userObj));
        toast.success("Cập nhật thành công!");
        navigate("/my-account");
      } else {
        toast.error("Cập nhật thất bại!");
      }
    } catch (err) {
      toast.error("Có lỗi xảy ra!");
    }
  };

  useEffect(() => {
    fetchProfile();
  }, []);

  return (
    <Container className="pt-4">
      <Card className="p-4 shadow-sm">
        <h4 className="mb-4">Cập nhật thông tin cá nhân</h4>

        <p>
          Email: <strong>{formData.email}</strong> <span style={{ fontStyle: "italic", color: "gray" }}>(Chúng tôi không hỗ trợ đổi email. Vui lòng liên hệ nếu muốn đổi account.)</span>
        </p>

        <Form onSubmit={handleSubmit}>
          <Form.Group className="mb-3">
            <Form.Label>Họ và Tên</Form.Label>
            <Form.Control type="text" name="fullName" value={formData.fullName} onChange={handleChange} placeholder="Nhập họ tên" />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Về bản thân</Form.Label>
            <Form.Control as="textarea" name="about" rows={3} placeholder="Chia sẻ những điều thú vị về bạn" value={formData.about} onChange={handleChange} />
          </Form.Group>

          <Form.Group className="mb-3">
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
          </Form.Group>

          <Button type="submit" variant="primary">
            Lưu
          </Button>
        </Form>
      </Card>
    </Container>
  );
};

export default Setting;
