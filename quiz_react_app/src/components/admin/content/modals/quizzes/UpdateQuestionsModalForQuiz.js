import { useEffect, useState } from "react";
import { Modal, Table, Form, Button, Badge } from "react-bootstrap";
import { toast } from "react-toastify";
import CustomPagination from "../../CustomPagination";
import FormQuestionFilter from "../../question/FormQuestionFilter";

const UpdateQuestionsForQuizModal = ({ show, setShow, questions, updatedQuestionIds, setUpdatedQuestionIds, fetchAllQuestions }) => {
  const [selectedIds, setSelectedIds] = useState([]);
  const [metadata, setMetadata] = useState({});
  const [filter, setFilter] = useState({ id: "", context: "", quizId: "" });

  useEffect(() => {
    if (Array.isArray(updatedQuestionIds)) {
      setSelectedIds(updatedQuestionIds);
    } else {
      setSelectedIds([]);
    }
  }, [updatedQuestionIds]);

  const handleSelect = (questionId) => {
    setSelectedIds((prev) => (prev.includes(questionId) ? prev.filter((id) => id !== questionId) : [...prev, questionId]));
  };

  const handleClose = () => {
    setShow(false);
    setSelectedIds([]);
  };

  const handleSubmit = () => {
    if (selectedIds.length < 1) {
      toast.warning("Bạn phải chọn ít nhất 1 câu hỏi!");
      return;
    }

    setUpdatedQuestionIds(selectedIds);
    setShow(false);
  };

  const handleSearch = () => {
    fetchAllQuestions(1, filter);
  };

  const handleClear = () => {
    const reset = { id: "", context: "", quizId: "" };
    setFilter(reset);
    fetchAllQuestions(1, reset);
  };

  const onPageChange = (page, f) => {
    if (page === metadata.currentPage) return;
    fetchAllQuestions(page, f);
  };

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Chọn câu hỏi cho Quiz</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <FormQuestionFilter filter={filter} setFilter={setFilter} handleSearch={handleSearch} handleClear={handleClear} />

        <Table bordered hover className="mt-3">
          <thead>
            <tr>
              <th>Chọn</th>
              <th>Nội dung</th>
              <th>ID</th>
              <th>Thuộc quiz</th>
            </tr>
          </thead>
          <tbody>
            {questions.length > 0 ? (
              questions.map((q) => (
                <tr key={q.questionId}>
                  <td>
                    <Form.Check type="checkbox" checked={selectedIds.includes(q.questionId)} onChange={() => handleSelect(q.questionId)} />
                  </td>
                  <td>{q.context}</td>
                  <td>
                    <Badge bg="secondary">{q.questionId}</Badge>
                  </td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={4} className="text-center text-muted">
                  Không có câu hỏi nào phù hợp.
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </Modal.Body>
      <Modal.Footer className="d-flex justify-content-between align-items-center">
        <CustomPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
        <div className="d-flex gap-2">
          <Button variant="secondary" onClick={handleClose}>
            Hủy
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            Xác nhận lựa chọn
          </Button>
        </div>
      </Modal.Footer>
    </Modal>
  );
};

export default UpdateQuestionsForQuizModal;
