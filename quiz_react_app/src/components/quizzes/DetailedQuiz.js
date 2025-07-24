import React, { useEffect, useState } from "react";
import { useParams, Link, useNavigate } from "react-router-dom";
import { showDetailQuiz } from "../../services/QuizServices";
import { toast } from "react-toastify";
import { Spinner, Table, Badge, Tab, Nav, Row, Col, Card, Alert, Button } from "react-bootstrap";
import UserBox from "./UserBox";
import { Form } from "react-bootstrap";
import { HiOutlineLightBulb } from "react-icons/hi";
const DetailedQuiz = () => {
  const { id } = useParams();
  const [detail, setDetail] = useState({});
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [avatar, setAvatar] = useState("");
  const [username, setUsername] = useState("");
  const [message, setMessage] = useState("Here's your progress!");
  const [timeModifier, setTimeModifier] = useState("");
  const navigate = useNavigate();

  useEffect(() => {
    const userStr = localStorage.getItem("user");
    if (userStr) {
      try {
        const user = JSON.parse(userStr);
        setAvatar(user.avatar || "");
        setUsername(user.fullName || "");
      } catch (err) {
        console.error("Error parsing user", err);
      }
    }
  }, []);

  useEffect(() => {
    const fetchDetail = async () => {
      setLoading(true);
      try {
        const res = await showDetailQuiz(id);
        console.log("CHECK RES", res);
        if (res.statusCode === 200) {
          setDetail(res.data);
          setResults(res.data.results || []);
        } else {
          toast.warning("Không lấy được dữ liệu chi tiết bài quiz.");
        }
      } catch (err) {
        toast.error("Lỗi khi gọi API chi tiết bài quiz.");
      } finally {
        setLoading(false);
      }
    };

    fetchDetail();
  }, [id]);

  const formatTime = (seconds) => {
    if (!seconds || seconds === 0) return "0:00";
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, "0")}`;
  };

  const handleSelectTimeModifier = (time) => {
    setTimeModifier(time);
  };

  const TimeModifierSelect = () => {
    return (
      <Form.Select className="my-4" onChange={(e) => handleSelectTimeModifier(e.target.value)} value={timeModifier}>
        <option value="">Chọn thời gian</option>
        <option value="infinity">Không giới hạn</option> {/* Đổi label cho rõ ràng */}
        <option value="20">20 phút</option>
        <option value="25">25 phút</option>
        <option value="30">30 phút</option>
        <option value="35">35 phút</option>
        <option value="40">40 phút</option>
        <option value="45">45 phút</option>
        <option value="50">50 phút</option>
        <option value="55">55 phút</option>
        <option value="60">60 phút</option>
        <option value="65">65 phút</option>
        <option value="70">70 phút</option>
      </Form.Select>
    );
  };

  const handlePractice = (timeModifier) => {
    navigate(`/do-quiz/${id}?time=${timeModifier}`);
  };
  const handleTest = () => {
    navigate(`/do-quiz/${id}`);
  };
  return (
    <div className="container mt-4">
      <Row>
        <Col md={9}>
          <Card className="p-5">
            <h3 className="text-primary">{detail.quizTitle || "Chi tiết bài quiz"}</h3>
            <div className="mb-5">
              <p>
                <div>
                  <strong>Thời gian làm bài:</strong> {detail.timeLimit} phút | <strong>Số câu hỏi:</strong> {detail.totalQuestions} | <strong>Phần thi:</strong> {detail.totalParts} |{" "}
                  <strong>Bình luận:</strong> {detail.totalComments}
                </div>
                <div>
                  <strong>Người đã tham gia:</strong> <span>{detail.totalParticipants}</span>
                </div>
              </p>
            </div>
            <div style={{ fontSize: "20px" }}>
              <strong>Kết quả làm bài của bạn:</strong>
            </div>
            <br />

            {loading ? (
              <div className="text-center py-4">
                <Spinner animation="border" />
              </div>
            ) : results.length === 0 ? (
              <em>Chưa có kết quả luyện tập nào.</em>
            ) : (
              <Table striped bordered hover>
                <thead>
                  <tr>
                    <th>Ngày làm</th>
                    <th>Kết quả</th>
                    <th>Thời gian làm bài</th>
                    <th>Xem chi tiết</th>
                  </tr>
                </thead>
                <tbody>
                  {results.map((item, index) => (
                    <tr key={index}>
                      <td>{new Date(item.submittedDate).toLocaleDateString()}</td>
                      <td>
                        {item.score}/{item.totalQuestions}
                      </td>
                      <td>{formatTime(item.duration)}</td>
                      <td>
                        <Link to={`/results/${item.resultId}`}>Xem chi tiết</Link>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            )}

            <Tab.Container defaultActiveKey="tab-practice">
              <Nav variant="tabs">
                <Nav.Item>
                  <Nav.Link eventKey="tab-practice">Luyện tập</Nav.Link>
                </Nav.Item>
                <Nav.Item>
                  <Nav.Link eventKey="tab-fulltest">Làm Full Test</Nav.Link>
                </Nav.Item>
              </Nav>

              <Tab.Content className="mt-3">
                <Tab.Pane eventKey="tab-practice">
                  <Alert variant="success" className="d-flex align-items-start" style={{ borderRadius: "8px" }}>
                    {/* Hoặc dùng trực tiếp icon Unicode */}
                    <span style={{ fontSize: "24px", marginRight: "10px", lineHeight: "1" }}>💡</span>

                    <div>
                      <strong>Pro tips:</strong> Hình thức luyện tập từng phần và chọn mức thời gian phù hợp sẽ giúp bạn tập trung vào giải đúng các câu hỏi thay vì phải chịu áp lực hoàn thành bài
                      thi.
                    </div>
                  </Alert>
                  <TimeModifierSelect className="mb-3" />
                  <Button className="btn btn-primary" onClick={() => handlePractice(timeModifier)}>
                    Luyện tập
                  </Button>
                </Tab.Pane>

                <Tab.Pane eventKey="tab-fulltest">
                  <div className="mt-2">
                    <p>Full test mô phỏng đề thi thật với thời gian và số lượng câu hỏi tương ứng.</p>
                    <p>
                      <strong>Ghi chú:</strong> Làm full test sẽ được quy đổi sang thang điểm chuẩn như
                      <Badge bg="success" className="ms-1">
                        IELTS 9.0
                      </Badge>
                    </p>
                  </div>
                  <Button className="btn btn-primary" onClick={handleTest}>
                    Bắt đầu thi
                  </Button>
                </Tab.Pane>
              </Tab.Content>
            </Tab.Container>
          </Card>
        </Col>

        <Col md={3}>
          <div className="d-flex justify-content-between align-items-center mb-4">
            <UserBox avatarUrl={avatar} username={username} message={message} />
          </div>
        </Col>
      </Row>
    </div>
  );
};

export default DetailedQuiz;
