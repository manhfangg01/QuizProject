import { Table, Button, Badge } from "react-bootstrap";
import { getQuestionById } from "../../../../services/QuestionServices";
import { Bounce, toast } from "react-toastify";
import CustomPagination from "../CustomPagination";

const TableQuestions = ({ fetchQuestions, questions, metadata, onEdit, onDelete, onDetail, filter }) => {
  const showToast = (type, message) => {
    toast[type](message, {
      position: "top-center",
      autoClose: 5000,
      hideProgressBar: false,
      closeOnClick: true,
      pauseOnHover: true,
      draggable: true,
      theme: "light",
      transition: Bounce,
    });
  };

  const handleFetchOneQuestion = async (questionId) => {
    if (!questionId) onDetail(true, null);

    try {
      const res = await getQuestionById(questionId);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        onDetail(true, res.data);
      } else {
        showToast("warning", "Cannot display question details.");
      }
    } catch (err) {
      console.error("Error fetching question:", err);
      showToast("error", err?.response?.data?.message || "An error occurred!");
    }
  };

  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    fetchQuestions(pageNumber);
  };

  // Function to truncate long text
  const truncateText = (text, maxLength = 50) => {
    if (!text) return "";
    if (text.length <= maxLength) return text;
    return `${text.substring(0, maxLength)}...`;
  };

  return (
    <>
      <Table striped bordered hover responsive className="mt-3">
        <thead className="table-dark">
          <tr>
            <th>#</th>
            <th>Question Content</th>
            <th>Options Count</th>
            <th>Quiz ID</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {questions && questions.length > 0 ? (
            questions.map((question, index) => (
              <tr key={question.questionId}>
                <td>{index + 1}</td>
                <td>{truncateText(question.context)}</td>
                <td>
                  <Badge bg="info" pill>
                    {question.options?.length || 0}
                  </Badge>
                </td>
                <td>{question.quizId || "-"}</td>
                <td className="text-center" style={{ display: "flex", justifyContent: "space-around" }}>
                  <Button variant="info" size="sm" className="me-2" onClick={() => handleFetchOneQuestion(question.questionId)}>
                    Detail
                  </Button>
                  <Button variant="warning" size="sm" className="me-2" onClick={() => onEdit(true, question)}>
                    Edit
                  </Button>
                  <Button variant="danger" size="sm" onClick={() => onDelete(true, question.questionId)}>
                    Delete
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="5" className="text-center">
                No questions found.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
      <CustomPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
    </>
  );
};

export default TableQuestions;
