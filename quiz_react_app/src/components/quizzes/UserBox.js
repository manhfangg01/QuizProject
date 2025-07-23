import React from "react";
import { Card, Button } from "react-bootstrap";
import { Link } from "react-router-dom";

const UserBox = ({ avatarUrl, username, message }) => {
  return (
    <Card className="text-center shadow-sm">
      <Card.Body>
        <img src={avatarUrl} alt="avatar" className="rounded-circle mb-3" style={{ width: "70px", height: "70px", objectFit: "cover" }} />
        <h6>{username}</h6>
        <p style={{ fontSize: "0.9rem", color: "gray" }}>{message}</p>
        <Link to={"/user/personal-statistic"}>
          <Button variant="outline-primary" size="sm">
            📊 Thống kê kết quả
          </Button>
        </Link>
      </Card.Body>
    </Card>
  );
};

export default UserBox;
