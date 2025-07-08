import { useEffect, useState } from "react";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";
import { getOptionsByIds } from "../../../../../services/OptionService";

const DetailQuestionModal = ({ show, setShow, questionData, selectedOptions }) => {
  const [options, setOptions] = useState([]);

  const handleClose = () => {
    setShow(false);
  };

  useEffect(() => {
    const fetchOptions = async () => {
      if (selectedOptions && selectedOptions.length > 0) {
        try {
          const res = await getOptionsByIds(selectedOptions);
          if (res.statusCode === 200) {
            setOptions(res.data); // giả sử backend trả về mảng option
          }
        } catch (err) {
          console.error("Lỗi khi gọi API lấy options:", err);
        }
      } else {
        setOptions([]);
      }
    };

    if (show) {
      fetchOptions();
    }
  }, [selectedOptions, show]);

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="lg">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết câu hỏi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {questionData ? (
          <>
            <div className="mb-3">
              <strong>ID:</strong> {questionData.id}
            </div>
            <div className="mb-3">
              <strong>Nội dung:</strong> {questionData.context}
            </div>

            <hr />
            <h5>Danh sách lựa chọn:</h5>
            {options && options.length > 0 ? (
              <ul>
                {options.map((opt) => (
                  <li key={opt.id}>
                    <strong>{opt.context}</strong> {opt.isCorrect ? "(Đúng)" : "(Sai)"}
                  </li>
                ))}
              </ul>
            ) : (
              <div className="text-muted">Không có lựa chọn nào.</div>
            )}
          </>
        ) : (
          <div className="text-muted">Không có dữ liệu câu hỏi.</div>
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

export default DetailQuestionModal;
