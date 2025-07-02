import { FaFacebookF, FaGoogle, FaGithub } from "react-icons/fa";
import { Form, Button, Card, Container, Row, Col } from "react-bootstrap";
import { Link, useNavigate } from "react-router-dom"; // Nếu dùng React Router
import "./auth.scss";
import { useState } from "react";
import axios from "axios";

const Login = () => {
  const navigate = useNavigate(); // hook chuyển trang
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const handleOnSubmit = async (event) => {
    event.preventDefault(); // chặn reload mặc định

    try {
      const res = await axios.post("http://localhost:8080/api/auth/login", {
        username,
        password,
      });

      // 🔄 Nếu đăng nhập thành công (có thể kiểm tra res.status hoặc dữ liệu token...)
      if (res.status === 200) {
        const token = res.data.data.accessToken;
        localStorage.setItem("accessToken", token);
        setTimeout(() => navigate("/"), 100); // delay nhẹ để đảm bảo token đã được lưu
      }
    } catch (error) {
      console.error("Login failed:", error);
      // Có thể show lỗi bằng toast / alert ở đây
    }
  };
  return (
    <Container fluid className="d-flex vh-100 justify-content-center align-items-center bg-light">
      <Row className="w-100 justify-content-center">
        <Col xs={11} sm={8} md={6} lg={4}>
          <Card className="p-4 shadow-lg rounded-4">
            <Card.Body>
              <h3 className="text-center mb-4">Login</h3>
              <Form>
                <Form.Group className="mb-3" controlId="formBasicEmail">
                  <Form.Label>Email address</Form.Label>
                  <Form.Control
                    type="email"
                    placeholder="Enter email"
                    size="lg"
                    value={username}
                    onChange={(event) => {
                      setUsername(event.target.value);
                    }}
                  />
                </Form.Group>

                <Form.Group className="mb-1" controlId="formBasicPassword">
                  <Form.Label>Password</Form.Label>
                  <Form.Control
                    type="password"
                    placeholder="Password"
                    size="lg"
                    value={password}
                    onChange={(event) => {
                      setPassword(event.target.value);
                    }}
                  />
                </Form.Group>

                <div className="d-flex justify-content-end mb-3">
                  <Link to="/forgot-password" className="text-decoration-none">
                    Forgot password?
                  </Link>
                </div>

                <Button
                  variant="primary"
                  type="submit"
                  size="lg"
                  className="w-100 mb-3"
                  onClick={(event) => {
                    handleOnSubmit(event);
                  }}
                >
                  Submit
                </Button>
              </Form>

              <div className="text-center mb-3 text-muted">Or login with</div>

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
                Don't have an account?{" "}
                <Link to="/signup" className="text-decoration-none">
                  Sign up
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
