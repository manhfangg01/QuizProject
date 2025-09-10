import { useEffect, useState } from "react";
import { Modal, Table, Form, Button } from "react-bootstrap";
import AdvancedPagination from "../../AdvancedPagination";
import FormQuestionFilter from "../../question/FormQuestionFilter";

const SelectQuestionsModalForQuiz = ({ show, setShow, questions = [], selectedQuestionIds, setSelectedQuestionIds, metadata, fetchAllQuestions }) => {
  const [selectedIds, setSelectedIds] = useState([]);
  const [filter, setFilter] = useState({
    id: "",
    context: "",
    quizId: "",
  });

  const handleClose = () => {
    setShow(false);
    setSelectedIds([]);
    setFilter({});
  };

  const handleSearch = () => {
    fetchAllQuestions(1, filter);
  };

  const handleClear = () => {
    const reset = { id: "", context: "", quizId: "" };
    setFilter(reset);
    fetchAllQuestions(1, reset);
  };

  // ✅ Đổi từ `id` → `questionId` để rõ ràng hơn
  const handleSelect = (questionId) => {
    setSelectedIds((prev) => (prev.includes(questionId) ? prev.filter((id) => id !== questionId) : [...prev, questionId]));
  };

  const handleSubmit = () => {
    setSelectedQuestionIds(selectedIds);
    setShow(false);
    setSelectedIds([]);
  };

  const onPageChange = (pageNumber, filter) => {
    if (pageNumber === metadata.currentPage) return;
    fetchAllQuestions(pageNumber, filter);
  };

  useEffect(() => {
    if (show) {
      setSelectedIds(selectedQuestionIds || []);
    }
  }, [show, selectedQuestionIds]);

  return (
    <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
      <Modal.Header closeButton>
        <Modal.Title>Chọn câu hỏi</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <div className="filter-manager mb-3">
          <FormQuestionFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
        </div>

        <Table striped bordered hover>
          <thead>
            <tr>
              <th>Chọn</th>
              <th>Nội dung</th>
              <th>ID</th>
            </tr>
          </thead>
          <tbody>
            {Array.isArray(questions) && questions.length > 0 ? (
              questions.map((question) => (
                <tr key={question.questionId}>
                  <td>
                    {/* ✅ Giữ nguyên `question.id` vì đây là dữ liệu từ API */}
                    <Form.Check type="checkbox" checked={selectedIds.includes(question.questionId)} onChange={() => handleSelect(question.questionId)} />
                  </td>
                  <td>{question.context}</td>
                  <td>{question.questionId}</td>
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={3} className="text-center">
                  Không có câu hỏi nào.
                </td>
              </tr>
            )}
          </tbody>
        </Table>
      </Modal.Body>

      <Modal.Footer>
        <div className="col-12">
          <AdvancedPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
        </div>
        <Button variant="secondary" onClick={handleClose}>
          Hủy
        </Button>
        <Button variant="primary" onClick={handleSubmit}>
          Xác nhận lựa chọn
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default SelectQuestionsModalForQuiz;
