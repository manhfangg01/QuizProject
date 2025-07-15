import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Alert, Button, Tabs, Tab } from "react-bootstrap";
import { useParams, useNavigate } from "react-router-dom"; // Nếu bạn cần lấy ID từ URL
import { getDetailResult } from "../../services/ResultServices"; // Hàm gọi API detail result
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

const ResultDetail = () => {
  const { id } = useParams(); // lấy ID từ URL
  const navigate = useNavigate();
  const [result, setResult] = useState(null);

  useEffect(() => {
    const fetchResultDetail = async () => {
      try {
        const res = await getDetailResult(id);
        if (res.statusCode === 200) {
          setResult(res.data);
        } else {
          toast.error("Không thể lấy dữ liệu chi tiết kết quả!");
        }
      } catch (err) {
        toast.error("Đã xảy ra lỗi khi gọi API!");
      }
    };

    if (id) fetchResultDetail();
  }, [id]);

  return (
    <Container>
      <Alert variant="warning">Bạn chưa tạo mục tiêu...</Alert>
      <Alert variant="success">Chú ý: Bạn có thể tạo flashcards...</Alert>

      <h5>Kết quả thi: {result?.quizTitle || "Loading..."}</h5>

      <div className="d-flex gap-2 mb-3">
        <Button variant="primary">Xem đáp án</Button>
        <Button variant="secondary" onClick={() => navigate(-1)}>
          Quay về trang hồ sơ
        </Button>
      </div>

      <Row>
        <Col>
          <Card>
            <Card.Body>
              <div>
                Kết quả làm bài:
                <strong>
                  {result?.totalCorrectedAnswers}/{result?.totalQuestions}
                </strong>
              </div>
              <div>
                Độ chính xác: <strong>{result?.accuracy?.toFixed(2)}%</strong>
              </div>
              <div className="d-flex align-items-center">
                <div>Thời gian hoàn thành:</div>
                <div style={{ fontSize: "20px" }}>
                  <strong>{formatDuration(result?.duration)}</strong>
                </div>
              </div>
            </Card.Body>
          </Card>
        </Col>
        <Col>
          <Card className="text-center">
            <Card.Body>
              <div style={{ fontSize: "1.5rem", color: "green" }}>✔️</div>
              <div>Trả lời đúng</div>
              <strong>{result?.totalCorrectedAnswers}</strong> câu hỏi
            </Card.Body>
          </Card>
        </Col>
        <Col>
          <Card className="text-center">
            <Card.Body>
              <div style={{ fontSize: "1.5rem", color: "red" }}>❌</div>
              <div>Trả lời sai</div>
              <strong>{result?.totalWrongAnswers}</strong> câu hỏi
            </Card.Body>
          </Card>
        </Col>
        <Col>
          <Card className="text-center">
            <Card.Body>
              <div style={{ fontSize: "1.5rem", color: "#888" }}>⏭️</div>
              <div>Bỏ qua</div>
              <strong>{result?.totalSkippedAnswers}</strong> câu hỏi
            </Card.Body>
          </Card>
        </Col>
        <Col>
          <Card className="text-center">
            <Card.Body>
              <div style={{ fontSize: "1.5rem", color: "#007bff" }}>🎯</div>
              <div>Điểm</div>
              <strong>{result?.score}</strong>
            </Card.Body>
          </Card>
        </Col>
      </Row>

      <h5 className="mt-4">Phân tích chi tiết</h5>
      <Tabs defaultActiveKey="recording1">
        <Tab eventKey="recording1" title="Recording 1">
          {/* Bảng chi tiết kết quả câu hỏi Recording 1 */}
        </Tab>
        <Tab eventKey="recording2" title="Recording 2">
          {/* Bảng chi tiết kết quả câu hỏi Recording 2 */}
        </Tab>
        <Tab eventKey="summary" title="Tổng quát">
          {/* Phân tích tổng thể */}
        </Tab>
      </Tabs>

      <div className="mt-3">
        <Button variant="outline-primary">Xem chi tiết đáp án</Button>
        <Button variant="outline-danger" className="ms-2">
          Làm lại các câu sai
        </Button>
        <p className="text-danger mt-2" style={{ fontStyle: "italic" }}>
          Chú ý: Khi làm lại các câu sai, điểm trung bình sẽ KHÔNG BỊ ẢNH HƯỞNG.
        </p>
        <Alert variant="success">📘 Tips: Khi xem chi tiết đáp án, bạn có thể tạo và lưu highlight từ vựng, keywords...</Alert>
      </div>
    </Container>
  );
};

// Utility: Chuyển giây sang định dạng hh:mm:ss
const formatDuration = (totalSeconds) => {
  if (!totalSeconds) return "0s";
  const hours = Math.floor(totalSeconds / 3600);
  const minutes = Math.floor((totalSeconds % 3600) / 60);
  const seconds = totalSeconds % 60;
  return `${hours}:${minutes}:${seconds}`;
};

export default ResultDetail;
