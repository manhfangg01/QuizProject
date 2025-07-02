import { FaFacebookF, FaGoogle, FaGithub } from "react-icons/fa";
import { Form, Button, Card, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom";
import "./auth.scss";
import axios from "axios";
import { useState } from "react";

const Signup = () => {
  const navigate = useNavigate(); // hook chuyển trang
  const [fullName, setFullName] = useState("");
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [error, setError] = useState("");

  const handleOnSubmit = async (event) => {
    event.preventDefault(); // chặn reload mặc định

    setError(""); // reset lỗi trước đó

    // 1. Kiểm tra confirm password
    if (password !== confirmPassword) {
      setError("Passwords do not match");
      return;
    }

    try {
      const res = await axios.post("http://localhost:8080/api/auth/signup", {
        fullName,
        email,
        password,
        confirmPassword,
      });

      // 🔄 Nếu đăng nhập thành công (có thể kiểm tra res.status hoặc dữ liệu token...)
      if (res.status === 200) {
        navigate("/login"); // 👉 chuyển về trang chủ
      }
    } catch (error) {
      console.error("Signup failed:", error);
      // Có thể show lỗi bằng toast / alert ở đây
    }
  };
  return (
    <Container fluid className="d-flex vh-100 justify-content-center align-items-center bg-light">
      <Row className="w-100 justify-content-center">
        <Col xs={11} sm={8} md={6} lg={4}>
          <Card className="p-4 shadow-lg rounded-4">
            <Card.Body>
              <h3 className="text-center mb-4">Sign up</h3>
              <Form>
                <Form.Group className="mb-3" controlId="formFullName">
                  <Form.Label>Full name</Form.Label>
                  <Form.Control
                    type="text"
                    placeholder="Enter your full name"
                    size="lg"
                    value={fullName}
                    onChange={(event) => {
                      setFullName(event.target.value);
                    }}
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formEmail">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    size="lg"
                    value={email}
                    onChange={(event) => {
                      setEmail(event.target.value);
                    }}
                  />
                </Form.Group>

                <Form.Group className="mb-3" controlId="formPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Enter password"
                    size="lg"
                    value={password}
                    onChange={(event) => {
                      setPassword(event.target.value);
                    }}
                  />
                </Form.Group>

                <Form.Group className="mb-4" controlId="formConfirmPassword">
                  <Form.Label>Confirm password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Re-enter password"
                    size="lg"
                    value={confirmPassword}
                    onChange={(event) => {
                      setConfirmPassword(event.target.value);
                    }}
                  />
                  {error && (
                    <div className="alert alert-danger text-center py-2" role="alert">
                      {error}
                    </div>
                  )}
                </Form.Group>

                <Button
                  variant="success"
                  type="submit"
                  size="lg"
                  className="w-100 mb-3"
                  onClick={(event) => {
                    handleOnSubmit(event);
                  }}
                >
                  Register
                </Button>
              </Form>

              <div className="text-center mb-3 text-muted">Or sign up with</div>

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
                Already have an account?{" "}
                <Link to="/login" className="text-decoration-none">
                  Log in
                </Link>
              </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default Signup;
