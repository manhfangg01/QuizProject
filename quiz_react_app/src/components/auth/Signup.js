import { FaFacebookF, FaGoogle, FaGithub } from "react-icons/fa";
import { Form, Button, Card, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import "./auth.scss";
import { useState } from "react";
import axios from "axios";
import { Bounce, toast, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const Signup = () => {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    fullName: "",
    email: "",
    password: "",
    confirmPassword: "",
  });
  const [errors, setErrors] = useState({});
  const [isLoading, setIsLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
    // Clear error when user types
    if (errors[name]) {
      setErrors((prev) => ({ ...prev, [name]: "" }));
    }
  };

  const validateForm = () => {
    const newErrors = {};

    if (!formData.fullName.trim()) {
      newErrors.fullName = "Full name is required";
    }

    if (!formData.email.trim()) {
      newErrors.email = "Email is required";
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(formData.email)) {
      newErrors.email = "Please enter a valid email";
    }

    if (!formData.password) {
      newErrors.password = "Password is required";
    } else if (formData.password.length < 6) {
      newErrors.password = "Password must be at least 6 characters";
    }

    if (formData.password !== formData.confirmPassword) {
      newErrors.confirmPassword = "Passwords do not match";
    }

    setErrors(newErrors);
    return Object.keys(newErrors).length === 0;
  };

  const handleOnSubmit = async (event) => {
    event.preventDefault();

    if (!validateForm()) return;

    setIsLoading(true);

    try {
      const res = await axios.post("http://localhost:8080/api/auth/signup", {
        fullName: formData.fullName,
        email: formData.email,
        password: formData.password,
      });

      if (res.status === 200) {
        toast.success("🎉 Đăng ký thành công! Vui lòng đăng nhập", {
          position: "top-center",
          autoClose: 3000,
          hideProgressBar: false,
          closeOnClick: true,
          pauseOnHover: true,
          draggable: true,
          progress: undefined,
          theme: "light",
          transition: Bounce,
          onClose: () => navigate("/login"),
        });
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || "Đăng ký thất bại. Vui lòng thử lại!";
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
              <h3 className="text-center mb-4">Đăng ký tài khoản</h3>
              <Form onSubmit={handleOnSubmit}>
                <Form.Group className="mb-3" controlId="formFullName">
                  <Form.Label>Họ và tên</Form.Label>
                  <Form.Control type="text" name="fullName" placeholder="Nhập họ và tên đầy đủ" size="lg" value={formData.fullName} onChange={handleInputChange} isInvalid={!!errors.fullName} />
                  <Form.Control.Feedback type="invalid">{errors.fullName}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3" controlId="formEmail">
                  <Form.Label>Email</Form.Label>
                  <Form.Control type="email" name="email" placeholder="Nhập địa chỉ email" size="lg" value={formData.email} onChange={handleInputChange} isInvalid={!!errors.email} />
                  <Form.Control.Feedback type="invalid">{errors.email}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-3" controlId="formPassword">
                  <Form.Label>Mật khẩu</Form.Label>
                  <Form.Control type="password" name="password" placeholder="Nhập mật khẩu" size="lg" value={formData.password} onChange={handleInputChange} isInvalid={!!errors.password} />
                  <Form.Control.Feedback type="invalid">{errors.password}</Form.Control.Feedback>
                </Form.Group>

                <Form.Group className="mb-4" controlId="formConfirmPassword">
                  <Form.Label>Xác nhận mật khẩu</Form.Label>
                  <Form.Control
                    type="password"
                    name="confirmPassword"
                    placeholder="Nhập lại mật khẩu"
                    size="lg"
                    value={formData.confirmPassword}
                    onChange={handleInputChange}
                    isInvalid={!!errors.confirmPassword}
                  />
                  <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                </Form.Group>

                <Button variant="success" type="submit" size="lg" className="w-100 mb-3" disabled={isLoading}>
                  {isLoading ? "Đang xử lý..." : "Đăng ký"}
                </Button>
              </Form>

              <div className="text-center mb-3 text-muted">Hoặc đăng ký bằng</div>

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
                Đã có tài khoản?{" "}
                <Link to="/login" className="text-decoration-none fw-bold">
                  Đăng nhập ngay
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <ToastContainer
        position="top-center"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={true}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
        transition={Bounce}
      />
    </Container>
  );
};

export default Signup;
