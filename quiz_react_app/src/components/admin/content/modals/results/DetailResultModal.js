import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { getResultById } from "../../../../../services/ResultServices";
import { useEffect, useState } from "react";
import { format } from "date-fns";

const DetailResultModal = ({ show, setShow, resultData }) => {
  const [fullResultData, setFullResultData] = useState({});

  const handleClose = () => {
    setShow(false);
  };

  const handleFetchFullDataForResult = async (resultData) => {
    try {
      const response = await getResultById(resultData.id);
      if (response.statusCode === 200) {
        setFullResultData(response.data);
      } else {
        console.warn("⚠️ Lấy dữ liệu kết quả thất bại:", response.message || "Không rõ nguyên nhân");
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API getResultById:", error);
    }
  };

  useEffect(() => {
    if (show && resultData?.id) {
      handleFetchFullDataForResult(resultData);
    }
  }, [show, resultData]);

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết kết quả #{resultData?.id}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {fullResultData ? (
          <>
            <div className="row">
              <div className="col-md-6">
                <div className="mb-3">
                  <strong>ID Kết quả:</strong> {fullResultData.id}
                </div>
                <div className="mb-3">
                  <strong>Người dùng:</strong> User #{fullResultData.userId}
                </div>
                <div className="mb-3">
                  <strong>Tên Người dùng:</strong> User #{fullResultData.userName}
                </div>
                <div className="mb-3">
                  <strong>Bài Quiz:</strong> Quiz #{fullResultData.quizId}
                </div>
                <div className="mb-3">
                  <strong>Tiêu đề:</strong> Quiz #{fullResultData.quizTilte}
                </div>
              </div>
              <div className="col-md-6">
                <div className="mb-3">
                  <strong>Điểm số:</strong> {fullResultData.score}/{fullResultData.totalQuestions}
                </div>
                <div className="mb-3">
                  <strong>Số câu đúng:</strong> {fullResultData.totalCorrectedAnswer}/{fullResultData.totalQuestions}
                </div>
                <div className="mb-3">
                  <strong>Thời gian làm:</strong> {fullResultData.duration} giây
                </div>
                <div className="mb-3">
                  <strong>Tỉ lệ đúng:</strong> {fullResultData.totalQuestions ? Math.round((fullResultData.totalCorrectedAnswer / fullResultData.totalQuestions) * 100) : 0}%
                </div>

                <div className="mb-3">
                  <strong>Ngày làm:</strong> {fullResultData.submittedAt && format(new Date(fullResultData.submittedAt), "dd/MM/yyyy HH:mm")}
                </div>
              </div>
            </div>

            <hr />

            <h5>Chi tiết từng câu hỏi:</h5>
            {fullResultData.questionDetails && fullResultData.questionDetails.length > 0 ? (
              <div className="table-responsive">
                <table className="table table-bordered">
                  <thead className="table-light">
                    <tr>
                      <th>Câu hỏi</th>
                      <th>Đáp án chọn</th>
                      <th>Đáp án đúng</th>
                      <th>Kết quả</th>
                    </tr>
                  </thead>
                  <tbody>
                    {fullResultData.questionDetails.map((q, index) => (
                      <tr key={index}>
                        <td>{q.questionText}</td>
                        <td>{q.selectedAnswer}</td>
                        <td>{q.correctAnswer}</td>
                        <td>{q.isCorrect ? <span className="text-success">Đúng</span> : <span className="text-danger">Sai</span>}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            ) : (
              <div className="text-muted">Không có chi tiết câu hỏi.</div>
            )}
          </>
        ) : (
          <div className="text-center py-4">
            <div className="spinner-border text-primary" role="status">
              <span className="visually-hidden">Loading...</span>
            </div>
            <p className="mt-2">Đang tải dữ liệu...</p>
          </div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Đóng
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DetailResultModal;
