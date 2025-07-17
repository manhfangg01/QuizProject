import { useEffect, useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { toast } from "react-toastify";
import { getDetailAnswer } from "../../services/AnswerService";

const DetailOptionModal = ({ show, setShow, answerId }) => {
  const handleClose = () => {
    // Reset tất cả state khi đóng modal
    setIsCorrect("");
    setQuestionContext("");
    setSelectedOptionLabel("");
    setCorrectedOptionLabel("");
    setSelectedOptionContext("");
    setCorrectedOptionContext("");
    setExplaination("");
    setShow(false);
  };
  const [isCorrect, setIsCorrect] = useState("");
  const [questionContext, setQuestionContext] = useState("");
  const [selectedOptionLabel, setSelectedOptionLabel] = useState("");
  const [correctedOptionLabel, setCorrectedOptionLabel] = useState("");
  const [selectedOptionContext, setSelectedOptionContext] = useState("");
  const [correctedOptionContext, setCorrectedOptionContext] = useState("");
  const [explaination, setExplaination] = useState("");

  const handleFetchDetailAnswer = async (answerId) => {
    try {
      const res = await getDetailAnswer(answerId);
      if (res.statusCode === 200) {
        const data = res.data;
        const selectedOption = data.options.find((opt) => opt.optionId === data.selectedOptionId);
        const correctedOption = data.options.find((opt) => opt.optionId === data.correctedOptionId);

        setQuestionContext(data?.questionContext);
        setSelectedOptionContext(selectedOption?.optionContext || "");
        setCorrectedOptionContext(correctedOption?.optionContext || "");
        setSelectedOptionLabel(data?.selectedOptionLabel);
        setCorrectedOptionLabel(data?.correctedOptionLabel);
        setIsCorrect(data?.isCorrect);
        setExplaination(data?.explaination || ""); // Thêm dòng này nếu bạn muốn hiển thị giải thích
      } else {
        toast.warning("Gọi API thất bại trong modal");
      }
    } catch (err) {
      toast.warning(err?.message || "Đã xảy ra lỗi");
    }
  };

  useEffect(() => {
    if (show && answerId) {
      // Chỉ gọi API khi modal hiển thị và có answerId
      handleFetchDetailAnswer(answerId);
    }
  }, [show, answerId]); // Chạy lại khi show hoặc answerId thay đổi

  return (
    <Modal show={show} onHide={handleClose} size="lg" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết câu trả lời</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="mb-3">
          <strong>Câu hỏi:</strong>
          <div>{questionContext}</div>
        </div>

        <div className="mb-3">
          <strong>Đáp án đúng:</strong>
          <div>
            {correctedOptionLabel}:{correctedOptionContext}
          </div>
        </div>

        <div className="mb-3">
          <strong>Đáp án của bạn:</strong>
          <div>
            {selectedOptionLabel}: {selectedOptionContext}
          </div>
        </div>

        <div className="mb-3">
          <strong>Kết quả:</strong>
          <div>{isCorrect ? <span className="text-success">Chính xác</span> : <span className="text-danger">Sai</span>}</div>
        </div>

        {explaination && (
          <div className="mb-3">
            <strong>Giải thích:</strong>
            <div>{explaination}</div>
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

export default DetailOptionModal;
