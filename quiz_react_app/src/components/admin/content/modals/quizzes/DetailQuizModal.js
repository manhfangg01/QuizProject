import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { getQuizById } from "../../../../../services/QuizServices";
import { useEffect, useState } from "react";
import AudioPlayer from "../../../../../utils/AudioPlayer";

const DetailQuizModal = ({ show, setShow, quizData }) => {
  const [fullQuizData, setFullQuizData] = useState({});
  const handleClose = () => {
    setShow(false);
  };
  const handleFetchFullDataForQuiz = async (quizData) => {
    try {
      const response = await getQuizById(quizData.quizId);
      if (response.statusCode === 200) {
        setFullQuizData(response.data);
      } else {
        console.warn("⚠️ Lấy dữ liệu quiz thất bại:", response.message || "Không rõ nguyên nhân");
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API getQuizById:", error);
    }
  };

  useEffect(() => {
    if (show && quizData?.quizId) {
      handleFetchFullDataForQuiz(quizData);
    }
  }, [show]);

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết bài Quiz</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {fullQuizData ? (
          <>
            <div className="mb-2">
              <strong>ID:</strong> {fullQuizData.quizId}
            </div>
            <div className="mb-2">
              <strong>Tiêu đề:</strong> {fullQuizData.title}
            </div>
            <div className="mb-2">
              <strong>Môn học:</strong> {fullQuizData.subjectName}
            </div>
            <div className="mb-2">
              <strong>Thời gian giới hạn:</strong> {fullQuizData.timeLimit} phút
            </div>
            <div className="mb-2">
              <strong>Số lượng người tham gia:</strong> {fullQuizData.totalParticipants}
            </div>
            <div className="mb-2">
              <strong>Trạng thái:</strong> {fullQuizData.isActive ? "Đang hoạt động" : "Đã tắt"}
            </div>
            <div className="mb-2">
              <strong>Độ khó:</strong> {fullQuizData.difficulty}
            </div>

            <hr />
            <h5>Danh sách câu hỏi:</h5>
            {fullQuizData.questions && fullQuizData.questions.length > 0 ? (
              <ul>
                {fullQuizData.questions.map((q) => (
                  <li key={q.questionId}>
                    ID: {q.questionId} — {q.context}
                  </li>
                ))}
              </ul>
            ) : (
              <div className="text-muted">Không có câu hỏi nào.</div>
            )}

            {fullQuizData.audioUrl ? (
              <div className="mb-2">
                <strong>Audio mô tả:</strong>
                <AudioPlayer audioUrl={fullQuizData.audioUrl} />
              </div>
            ) : (
              <div className="text-muted">Không có ghi âm nào</div>
            )}
          </>
        ) : (
          <div className="text-muted">Không có dữ liệu quiz.</div>
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

export default DetailQuizModal;
