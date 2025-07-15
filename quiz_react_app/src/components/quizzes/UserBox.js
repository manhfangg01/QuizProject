import React from "react";
import { Card, Button } from "react-bootstrap";

const UserBox = ({ avatarUrl, username, message }) => {
  return (
    <Card className="text-center shadow-sm">
      <Card.Body>
        <img src={avatarUrl} alt="avatar" className="rounded-circle mb-3" style={{ width: "70px", height: "70px", objectFit: "cover" }} />
        <h6>{username}</h6>
        <p style={{ fontSize: "0.9rem", color: "gray" }}>{message}</p>
        <Button variant="outline-primary" size="sm">
          📊 Thống kê kết quả
        </Button>
      </Card.Body>
    </Card>
  );
};

export default UserBox;
