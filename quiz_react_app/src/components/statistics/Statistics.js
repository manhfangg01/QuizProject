import { Tab, Nav, Row, Col, Container, Card } from "react-bootstrap";
import { GrBarChart } from "react-icons/gr";
import { getUserStatistic } from "../../services/UserServices";
import { useEffect, useState } from "react";
import { toast } from "react-toastify";
import "./statistic.scss";
import { PiBookOpenThin, PiTargetThin } from "react-icons/pi";
import { CiClock2 } from "react-icons/ci";
import { getHistory } from "../../services/ResultServices";
import { Link } from "react-router-dom";
import AdvancedPagination from "../admin/content/AdvancedPagination";
const Statistics = () => {
  const [statistic, setStatistic] = useState({});
  const [history, setHistory] = useState([]); // từ {} -> []
  const [metadata, setMetadata] = useState({});
  const handleFetchStatistic = async () => {
    try {
      const res = await getUserStatistic();
      console.log("CHECK RES statistic", res);
      if (res.statusCode === 200) {
        setStatistic(res?.data);
      } else {
        toast.error(res.message || "Failed to load quizzes");
      }
    } catch (err) {
      toast.error(err.message || "An error occurred while loading quizzes");
      console.error("Error loading quizzes:", err);
    }
  };

  const fetchHistoryUser = async (pageNumber) => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      try {
        const res = await getHistory(pageNumber, userInfo.id);
        if (res.statusCode === 200) {
          const { histories, metadata } = res.data;
          setHistory(histories); // cập nhật state
          setMetadata(metadata);
        } else {
          toast.warning("Gọi API thất bại");
        }
      } catch (err) {
        toast.warning("Lỗi khi gọi API lịch sử");
      }
    }
  };
  useEffect(() => {
    handleFetchStatistic();
    fetchHistoryUser(1);
  }, []);
  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    fetchHistoryUser(pageNumber);
  };

  const formatTime = (seconds) => {
    const h = Math.floor(seconds / 3600)
      .toString()
      .padStart(2, "0");
    const m = Math.floor((seconds % 3600) / 60)
      .toString()
      .padStart(2, "0");
    const s = Math.floor(seconds % 60)
      .toString()
      .padStart(2, "0");
    return `${h}:${m}:${s}`;
  };

  const renderIELTSStatistic = () => {
    return (
      <div className="content-container">
        <Row className="mt-4 g-3">
          {/* Số đề đã làm */}
          <Col md={2} className="h-100">
            <Card className="text-center h-100">
              <Card.Body>
                <div
                  className="result-score-box"
                  style={{
                    marginRight: "1rem",
                    minWidth: "150px",
                    maxWidth: "150px",
                    fontSize: "20px",
                  }}
                >
                  <div className="result-score-icon text-gray">
                    <PiBookOpenThin />
                  </div>
                  <div className="result-score-icontext text-gray">Số đề đã làm</div>
                  <div className="result-score-text">
                    <strong>{statistic.totalDoneQuizzes}</strong>
                  </div>
                  <div className="result-score-sub">
                    <span>đề thi</span>
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Col>

          {/* Thời gian luyện thi */}
          <Col md={2} className="h-100">
            <Card className="text-center h-100">
              <Card.Body>
                <div
                  className="result-score-box"
                  style={{
                    marginRight: "1rem",
                    minWidth: "150px",
                    maxWidth: "150px",
                    fontSize: "20px",
                  }}
                >
                  <div className="result-score-icon text-gray">
                    <CiClock2 />
                  </div>
                  <div className="result-score-icontext text-gray">Thời gian luyện thi</div>
                  <div className="result-score-text">
                    <strong>{statistic?.totalSpentTime}</strong>
                  </div>
                  <div className="result-score-sub">
                    <span>phút</span>
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Col>

          {/* Điểm mục tiêu */}
          <Col md={2} className="h-100">
            <Card className="text-center h-100">
              <Card.Body>
                <div
                  className="result-score-box"
                  style={{
                    marginRight: "1rem",
                    minWidth: "150px",
                    maxWidth: "150px",
                    fontSize: "20px",
                  }}
                >
                  <div className="result-score-icon text-gray">
                    <PiTargetThin />
                  </div>
                  <div className="result-score-icontext text-gray">Điểm mục tiêu</div>
                  <div className="result-score-text">
                    <strong>{statistic?.goal || "Tạo ngay"}</strong>
                  </div>
                  <div className="result-score-sub">
                    <span>điểm</span>
                  </div>
                </div>
              </Card.Body>
            </Card>
          </Col>
        </Row>

        <Row className="mt-4 g-3">
          <Col md={2}>
            <Card className="text-center">
              <Card.Body style={{ minHeight: "150px" }}>
                <div className="text-muted">Độ chính xác (#đúng/#tổng)</div>
                <h4 className="fw-bold">{(statistic?.accuracy * 100).toFixed(2)}%</h4>
              </Card.Body>
            </Card>
          </Col>

          <Col md={2}>
            <Card className="text-center">
              <Card.Body style={{ minHeight: "150px" }}>
                <div className="text-muted">Thời gian trung bình</div>
                <h4 className="fw-bold">{formatTime(statistic?.averageTime)}</h4>
              </Card.Body>
            </Card>
          </Col>

          <Col md={2}>
            <Card className="text-center">
              <Card.Body style={{ minHeight: "150px" }}>
                <div className="text-muted">Điểm trung bình</div>
                <h4 className="fw-bold">{statistic?.averageMark}/10.0</h4>
              </Card.Body>
            </Card>
          </Col>

          <Col md={2}>
            <Card className="text-center">
              <Card.Body style={{ minHeight: "150px" }}>
                <div className="text-muted">Điểm cao nhất</div>
                <h4 className="fw-bold">{statistic?.highestMark}/10.0</h4>
              </Card.Body>
            </Card>
          </Col>
        </Row>
        <Row className="mt-4 g-3">
          <div style={{ fontSize: "30px", paddingTop: "20px" }}>
            <div style={{ marginBottom: "30px" }}>
              {" "}
              <strong>Danh sách đề thi đã làm: </strong>
            </div>
            {history.length === 0 ? (
              <em>Chưa có kết quả luyện thi nào.</em>
            ) : (
              <>
                <table className="table table-striped">
                  <thead>
                    <tr>
                      <th>Ngày làm</th>
                      <th>Đề thi</th>
                      <th>Kết quả</th>
                      <th>Thời gian làm bài</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {history.map((item, index) => (
                      <tr key={index}>
                        <td>{new Date(item.submittedAt).toISOString().slice(0, 10)}</td>
                        <td>{item.quizTitle}</td>
                        <td>
                          {item.totalCorrect}/{item.totalQuestions}
                          {item.score && ` (Điểm: ${item.score})`}
                        </td>
                        <td>{formatTime(item.duration)}</td>
                        <td>
                          <Link to={`/results/${item.resultId}`}>Xem chi tiết</Link>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>

                <AdvancedPagination metadata={metadata} onPageChange={onPageChange} />
              </>
            )}
          </div>
        </Row>
      </div>
    );
  };
  return (
    <div className="d-flex justify-content-center statistic-container">
      <Container className="bg-light border-bottom py-4 px-4" style={{ maxWidth: "1140px" }}>
        {/* Tiêu đề */}
        <div className="d-flex align-items-center mb-3">
          <GrBarChart size={28} className="me-2" />
          <h4 className="fw-bold m-0">Thống kê kết quả luyện thi</h4>
        </div>

        {/* Tabs */}
        <Tab.Container defaultActiveKey="ielts">
          <Nav variant="tabs">
            <Nav.Item>
              <Nav.Link eventKey="ielts">IELTS Academic</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="toeic">TOEIC</Nav.Link>
            </Nav.Item>
          </Nav>

          <Tab.Content>
            <Tab.Pane eventKey="ielts"> {renderIELTSStatistic()}</Tab.Pane>
            <Tab.Pane eventKey="toeic">
              <p>Nội dung thống kê TOEIC ở đây...</p>
            </Tab.Pane>
          </Tab.Content>
        </Tab.Container>
      </Container>
    </div>
  );
};
export default Statistics;
