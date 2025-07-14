import { Card, Container, Row, Col, Button } from "react-bootstrap";
import { useEffect, useState } from "react";
import { getProfile } from "../../../services/UserServices";
const AdminProfile = () => {
  const [user, setUser] = useState({
    id: "",
    fullName: "",
    email: "",
    avatar: "",
    role: "",
    createdAt: "",
  });

  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      fetchUser(userInfo.id);
    }
  }, []);

  const fetchUser = async (id) => {
    const res = await getProfile(id);
    if (res.statusCode === 200) {
      const data = res.data;
      setUser({
        id: data.id,
        fullName: data.fullName,
        email: data.email,
        avatar: data.avatar,
        role: data.role || "ADMIN",
        createdAt: data.createdAt?.slice(0, 10) || "",
      });
    }
  };
  return (
    <Container className="py-4">
      <Card className="shadow rounded-4 p-4">
        <Row className="align-items-center">
          <Col md={4} className="text-center">
            <img src={user.avatar || ""} alt="avatar" className="rounded-circle border" style={{ width: 180, height: 180, objectFit: "cover" }} />
          </Col>
          <Col md={8}>
            <h3 className="mb-3">{user.fullName}</h3>
            <p className="mb-1">
              <strong>Email:</strong> {user.email}
            </p>
            <p className="mb-1">
              <strong>Vai trò:</strong> <span className="badge bg-danger">{user.role}</span>
            </p>
            {user.createdAt && (
              <p className="mb-3">
                <strong>Tham gia từ:</strong> {user.createdAt}
              </p>
            )}
            <Button variant="outline-primary" onClick={() => (window.location.href = "/admins/manage-profile/setting")}>
              Cập nhật thông tin
            </Button>
          </Col>
        </Row>
      </Card>
    </Container>
  );
};

export default AdminProfile;
