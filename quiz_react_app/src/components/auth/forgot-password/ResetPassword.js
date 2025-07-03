import React, { useState } from "react";
import { Bounce, toast } from "react-toastify";
import { Container, Row, Col, Card, Form, Button } from "react-bootstrap";
import { useSearchParams, useNavigate } from "react-router-dom"; // Thêm useNavigate
import { FaRegEye, FaRegEyeSlash } from "react-icons/fa";
import { PostResetPassword } from "../../../services/AuthServices";

const ResetPassword = () => {
  const [newPassword, setNewPassword] = useState("");
  const [confirmPassword, setConfirmPassword] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [errors, setErrors] = useState({});
  const [showPassword, setShowPassword] = useState(false);
  const [isSuccess, setIsSuccess] = useState(false); // Thêm state để kiểm tra thành công
  const navigate = useNavigate(); // Thêm hook navigate

  const [searchParams] = useSearchParams();
  const resetToken = searchParams.get("token");

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
    if (!newPassword) {
      newErrors.newPassword = "Password is required";
    } else if (newPassword.length < 6) {
      newErrors.newPassword = "Password must be at least 6 characters";
    }

    if (newPassword !== confirmPassword) {
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
      const res = await PostResetPassword(resetToken, newPassword);
      if (res.statusCode === 200) {
        showToast("success", res.message);
        setIsSuccess(true); // Đánh dấu thành công
      } else {
        showToast("error", res.error || "Có lỗi xảy ra.");
      }
    } catch (error) {
      const errorMessage = error.response?.data?.message || "Xác nhận thất bại. Vui lòng thử lại!";
      showToast("error", errorMessage);
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <Container fluid className="d-flex justify-content-center align-items-center bg-light" style={{ paddingTop: "70px", paddingBottom: "120px" }}>
      <Row className="w-100 justify-content-center">
        <Col xs={11} sm={8} md={6} lg={4}>
          <Card className="p-4 shadow-lg rounded-4">
            <Card.Body>
              <h3 className="text-center mb-4">Đổi mật khẩu</h3>

              {isSuccess ? (
                <div className="text-center">
                  <p className="text-success mb-4">Đổi mật khẩu thành công!</p>
                  <Button
                    variant="outline-primary"
                    size="lg"
                    className="w-100"
                    onClick={() => navigate("/login")} // Chuyển hướng về trang đăng nhập
                  >
                    Trở về Đăng nhập
                  </Button>
                </div>
              ) : (
                <Form onSubmit={handleOnSubmit}>
                  <Form.Group className="mb-3 position-relative" controlId="formPassword">
                    <Form.Label>Mật khẩu mới</Form.Label>
                    <Form.Control
                      type={showPassword ? "text" : "password"}
                      name="password"
                      placeholder="Nhập mật khẩu"
                      size="lg"
                      value={newPassword}
                      onChange={(e) => {
                        setNewPassword(e.target.value);
                        setErrors((prev) => ({ ...prev, newPassword: "" }));
                      }}
                      isInvalid={!!errors.newPassword}
                    />
                    <Form.Control.Feedback type="invalid">{errors.newPassword}</Form.Control.Feedback>
                    {newPassword &&
                      !errors.password &&
                      (showPassword ? (
                        <FaRegEyeSlash
                          onClick={() => setShowPassword(false)}
                          style={{ position: "absolute", top: "70%", right: "15px", transform: "translateY(-50%)", cursor: "pointer", fontSize: "25px" }}
                        />
                      ) : (
                        <FaRegEye
                          onClick={() => setShowPassword(true)}
                          style={{ position: "absolute", top: "70%", right: "15px", transform: "translateY(-50%)", cursor: "pointer", fontSize: "25px" }}
                        />
                      ))}
                  </Form.Group>

                  <Form.Group className="mb-3 position-relative" controlId="formConfirmPassword">
                    <Form.Label>Xác nhận Mật khẩu</Form.Label>
                    <Form.Control
                      type="password"
                      name="confirmPassword"
                      placeholder="Xác nhận lại mật khẩu"
                      size="lg"
                      value={confirmPassword}
                      onChange={(e) => {
                        setConfirmPassword(e.target.value);
                        setErrors((prev) => ({ ...prev, confirmPassword: "" }));
                      }}
                      isInvalid={!!errors.confirmPassword}
                    />
                    <Form.Control.Feedback type="invalid">{errors.confirmPassword}</Form.Control.Feedback>
                  </Form.Group>
                  <Button variant="primary" type="submit" size="lg" className="w-100 mb-3" disabled={isLoading}>
                    {isLoading ? "Đang Xác Nhận..." : "Xác Nhận"}
                  </Button>
                </Form>
              )}
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default ResetPassword;
