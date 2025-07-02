import { FaFacebookF, FaGoogle, FaGithub } from "react-icons/fa";
import { Form, Button, Card, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import "./auth.scss";
import { useState } from "react";
import axios from "axios";
import { Bounce, ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleOnSubmit = async (event) => {
    event.preventDefault();
    setIsLoading(true);

    try {
      const res = await axios.post("http://localhost:8080/api/auth/login", formData);

      if (res.status === 200) {
        const token = res.data.data.accessToken;
        localStorage.setItem("accessToken", token);

        toast.success("🦄 Đăng nhập thành công!", {
          position: "top-center",
          autoClose: 2000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
          onClose: () => navigate("/"),
        });
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || "Đăng nhập thất bại. Vui lòng thử lại!";
      toast.error(errorMessage, {
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
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container fluid className="d-flex vh-100 justify-content-center align-items-center bg-light">
      <Row className="w-100 justify-content-center">
        <Col xs={11} sm={8} md={6} lg={4}>
          <Card className="p-4 shadow-lg rounded-4">
            <Card.Body>
              <h3 className="text-center mb-4">Đăng nhập</h3>
              <Form onSubmit={handleOnSubmit}>
                <Form.Group className="mb-3" controlId="formUsername">
                  <Form.Label>Tên đăng nhập hoặc Email</Form.Label>
                  <Form.Control type="text" name="username" placeholder="Nhập tên đăng nhập hoặc email" size="lg" value={formData.username} onChange={handleInputChange} required />
                </Form.Group>

                <Form.Group className="mb-1" controlId="formPassword">
                  <Form.Label>Mật khẩu</Form.Label>
                  <Form.Control type="password" name="password" placeholder="Nhập mật khẩu" size="lg" value={formData.password} onChange={handleInputChange} required minLength={6} />
                </Form.Group>

                <div className="d-flex justify-content-end mb-3">
                  <Link to="/forgot-password" className="text-decoration-none">
                    Quên mật khẩu?
                  </Link>
                </div>

                <Button variant="primary" type="submit" size="lg" className="w-100 mb-3" disabled={isLoading}>
                  {isLoading ? "Đang đăng nhập..." : "Đăng nhập"}
                </Button>
              </Form>

              <div className="text-center mb-3 text-muted">Hoặc đăng nhập bằng</div>

              <div className="d-flex justify-content-center gap-3 mb-3">
                <Button variant="outline-primary" className="rounded-circle d-flex align-items-center justify-content-center" style={{ width: 48, height: 48 }}>
                  <FaFacebookF />
                </Button>
                <Button variant="outline-danger" className="rounded-circle d-flex align-items-center justify-content-center" style={{ width: 48, height: 48 }}>
                  <FaGoogle />
                </Button>
                <Button variant="outline-dark" className="rounded-circle d-flex align-items-center justify-content-center" style={{ width: 48, height: 48 }}>
                  <FaGithub />
                </Button>
              </div>

              <div className="text-center">
                Chưa có tài khoản?{" "}
                <Link to="/signup" className="text-decoration-none fw-bold">
                  Đăng ký ngay
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
      <ToastContainer />
    </Container>
  );
};

export default Login;
