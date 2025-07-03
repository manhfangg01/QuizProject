import { FaFacebookF, FaGoogle, FaGithub, FaRegEyeSlash, FaRegEye } from "react-icons/fa";
import { Form, Button, Card, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import "./auth.scss";
import { useState } from "react";
import axios from "axios";
import { Bounce, ToastContainer, toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { postLogin } from "../../services/AuthServices";

const Login = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    username: "",
    password: "",
  });
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
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

  const validateForm = () => {
    const newErrors = {};

    if (!formData.username.trim()) {
      newErrors.username = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.username)) {
      newErrors.username = "Please enter a valid username";
    }

    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleOnSubmit = async (event) => {
    event.preventDefault();
    if (!validateForm()) return;
    setIsLoading(true);

    try {
      const res = await postLogin(formData.username, formData.password);

      console.log(res); // res chỉ là `data` do interceptor đã rút gọn
      if (res.statusCode === 200) {
        const token = res.data.accessToken;
        localStorage.setItem("accessToken", token);

        showToast("success", "Đăng nhập thành công !");
        navigate("/");
      } else {
        // fallback nếu backend không trả statusCode rõ ràng
        showToast("error", res.error || "Có lỗi xảy ra.");
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || "Đăng nhập thất bại. Vui lòng thử lại!";
      showToast("error", errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container fluid className="d-flex  justify-content-center align-items-center bg-light" style={{ paddingTop: "70px", paddingBottom: "120px" }}>
      <Row className="w-100 justify-content-center">
        <Col xs={11} sm={8} md={6} lg={4}>
          <Card className="p-4 shadow-lg rounded-4">
            <Card.Body>
              <h3 className="text-center mb-4">Đăng nhập</h3>
              <Form onSubmit={handleOnSubmit}>
                <Form.Group className="mb-3" controlId="formUsername">
                  <Form.Label>Tên đăng nhập hoặc Email</Form.Label>
                  <Form.Control
                    type="text"
                    name="username"
                    placeholder="Nhập tên đăng nhập hoặc email"
                    size="lg"
                    value={formData.username}
                    onChange={handleInputChange}
                    isInvalid={!!errors.username}
                  />
                  <Form.Control.Feedback type="invalid">{errors.username}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3 position-relative" controlId="formPassword">
                  <Form.Label>Mật khẩu</Form.Label>
                  <Form.Control
                    type={showPassword ? "text" : "password"}
                    name="password"
                    placeholder="Nhập mật khẩu"
                    size="lg"
                    value={formData.password}
                    onChange={handleInputChange}
                    isInvalid={!!errors.password}
                  />
                  <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                  {formData.password &&
                    !errors.password &&
                    (showPassword ? (
                      <FaRegEyeSlash
                        onClick={() => setShowPassword(false)}
                        style={{ position: "absolute", top: "70%", right: "15px", transform: "translateY(-50%)", cursor: "pointer", fontSize: "25px" }}
                      />
                    ) : (
                      <FaRegEye onClick={() => setShowPassword(true)} style={{ position: "absolute", top: "70%", right: "15px", transform: "translateY(-50%)", cursor: "pointer", fontSize: "25px" }} />
                    ))}
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
    </Container>
  );
};

export default Login;
