import React, { useState } from "react";
import { Bounce, toast } from "react-toastify";
import { callCheckEmail } from "../../../services/AuthServices";
import { Container, Row, Col, Card, Form, Button } from "react-bootstrap";
import "./CheckingEmail.scss";

const CheckingEmail = () => {
  const [username, setUsername] = useState("");
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState("");

  const showToast = (type, message) => {
    toast[type](message, {
      position: "top-center",
      autoClose: 10000,
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
    if (!username.trim()) {
      setError("Email is required");
      return false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(username)) {
      setError("Please enter a valid email address");
      return false;
    }
    return true;
    // Nếu chỉ có 1 thì nên trả thẳng về true false chứ không nên trả về Object
  };

  const handleOnSubmit = async (event) => {
    event.preventDefault();
    if (!validateForm()) return;
    setIsLoading(true);

    try {
      const res = await callCheckEmail(username);
      if (res.statusCode === 200) {
        showToast("success", res.data.message);
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
              <h3 className="text-center mb-4">Nhập Email khôi phục</h3>
              <Form onSubmit={handleOnSubmit}>
                <Form.Group className="mb-3" controlId="formUsername">
                  <Form.Control
                    type="text"
                    name="username"
                    placeholder="Email"
                    size="lg"
                    value={username}
                    onChange={(e) => {
                      setUsername(e.target.value);
                      setError(""); // Xóa lỗi khi người dùng nhập
                    }}
                    isInvalid={!!error}
                  />
                  <Form.Control.Feedback type="invalid">{error}</Form.Control.Feedback>
                </Form.Group>
                <Button variant="primary" type="submit" size="lg" className="w-100 mb-3" disabled={isLoading}>
                  {isLoading ? "Đang Xác Nhận..." : "Xác Nhận"}
                </Button>
              </Form>
            </Card.Body>
          </Card>
        </Col>
      </Row>
    </Container>
  );
};

export default CheckingEmail;
