import { useEffect, useState } from "react";
import { Container, Row, Col, Card, Alert, Button, Tabs, Tab } from "react-bootstrap";
import { useParams, useNavigate, Link } from "react-router-dom"; // Nếu bạn cần lấy ID từ URL
import { getDetailResult } from "../../services/ResultServices"; // Hàm gọi API detail result
import { toast } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { IoIosInformationCircleOutline } from "react-icons/io";
import DetailOptionModal from "./DetailOptionModal";

const ResultDetail = () => {
  const { id } = useParams(); // lấy ID từ URL
  const navigate = useNavigate();
  const [result, setResult] = useState(null);
  const [answerId, setAnswerId] = useState("");
  const [show, setShow] = useState(false);

  const handleShowDetailAnswer = (show, answerId) => {
    setAnswerId(answerId);
    setShow(show);
  };

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

  const ScrollToAnswers = (id) => {
    const element = document.getElementById("detailedAnswers");
    element?.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  const handleMoreDetail = (quizId) => {
    const token = localStorage.getItem("accessToken");
    const user = localStorage.getItem("user");

    if (!token || !user) {
      toast.warning("Please login to see more detail !");
      navigate("/login", { state: { from: `/detailedQuiz/${quizId}` } });
      return;
    }
    navigate(`/detailedQuiz/${quizId}`);
  };

  return (
    <Container>
      <Alert variant="warning">Bạn chưa tạo mục tiêu...</Alert>
      <Alert variant="success">Chú ý: Bạn có thể tạo flashcards...</Alert>

      <h5>Kết quả thi: {result?.quizTitle || "Loading..."}</h5>

      <div className="d-flex justify-content-between">
        <div className="d-flex gap-2 mb-3">
          <Button
            onClick={() => {
              ScrollToAnswers();
            }}
            variant="primary"
          >
            Xem đáp án
          </Button>
          <Link to={"/quizzes"} className="d-block h-100">
            <Button variant="secondary" className="h-100">
              Quay về trang đề thi
            </Button>
          </Link>
        </div>
        <div>
          <div style={{ textAlign: "center" }}>
            <Button variant="primary" className="h-100" onClick={() => handleMoreDetail(result?.quizId)}>
              Làm lại
            </Button>
            <div className="d-flex align-items-center gap-1">
              <IoIosInformationCircleOutline /> Kết quả của bạn sẽ được cập nhật lại sau khi làm
            </div>
          </div>
          <div></div>
        </div>
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

      <div className="mt-3">
        <h1 variant="outline-primary" id="detailedAnswers">
          Chi tiết đáp án
        </h1>
        <Alert variant="success">📘 Tips: Khi xem chi tiết đáp án, bạn có thể tạo và lưu highlight từ vựng, keywords...</Alert>
      </div>

      <div className="d-flex flex-wrap">
        {result?.answers.map((answer, index) => {
          const correctLabel = !answer.isCorrect && answer.correctedOptionLabel ? answer.correctedOptionLabel : null;
          const selectedOption = answer.options?.find((opt) => opt.optionId === answer.selectedOptionId);

          return (
            <div key={index} className="w-50 d-flex align-items-start mb-2">
              {/* Vòng tròn số thứ tự */}
              <div className="rounded-circle bg-primary text-white d-flex justify-content-center align-items-center me-2" style={{ width: "24px", height: "24px", fontSize: "14px" }}>
                {index + 1}
              </div>

              {/* Nội dung */}
              <div>
                <div>
                  <strong>{answer.selectedOptionLabel ?? "–"}:</strong> {selectedOption?.optionContext || <em>Chưa chọn</em>}{" "}
                  {answer.selectedOptionLabel && (answer.isCorrect ? <span className="text-success">✔</span> : <span className="text-danger">✘</span>)}
                </div>

                {/* Nếu sai => hiển thị đáp án đúng */}
                {correctLabel && (
                  <div className="text-muted small">
                    Đáp án đúng: <strong>{correctLabel}</strong>
                  </div>
                )}

                {/* Link chi tiết */}
                <Button
                  onClick={() => {
                    handleShowDetailAnswer(true, answer?.answerId);
                  }}
                >
                  [Chi tiết]
                </Button>
              </div>
            </div>
          );
        })}
      </div>
      <DetailOptionModal show={show} setShow={setShow} answerId={answerId} />
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
