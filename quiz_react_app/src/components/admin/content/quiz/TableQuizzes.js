import { Table, Button } from "react-bootstrap";
import { getQuizById } from "../../../../services/QuizServices";
import { Bounce, toast } from "react-toastify";
import CustomPagination from "../CustomPagination";

const TableQuiz = ({ fetchQuizzes, quizzes, metadata, onEdit, onDelete, onDetail }) => {
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

  const handleFetchOneQuiz = async (quizId) => {
    if (!quizId) onDetail(true, null);

    try {
      const res = await getQuizById(quizId);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        onDetail(true, res.data);
      } else {
        showToast("warning", res?.message || "Không thể hiển thị quiz.");
      }
    } catch (err) {
      console.error("❌ Lỗi khi fetch quiz:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi khi lấy quiz!");
    }
  };

  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    fetchQuizzes(pageNumber);
  };

  return (
    <>
      <Table striped bordered hover responsive className="mt-3" style={{ minHeight: "300px" }}>
        <thead className="table-dark">
          <tr>
            <th>STT</th>
            <th>Tiêu đề</th>
            <th>Môn học</th>
            <th>Thời gian (phút)</th>
            <th>Độ khó</th>
            <th>Người làm</th>
            <th>Trạng thái</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {quizzes && quizzes.length > 0 ? (
            quizzes.map((quiz) => (
              <tr key={quiz.quizId}>
                <td>{quiz.quizId}</td>
                <td>{quiz.title}</td>
                <td>{quiz.subjectName}</td>
                <td>{quiz.timeLimit}</td>
                <td>{quiz.difficulty}</td>
                <td>{quiz.totalParticipants}</td>
                <td>{quiz.isActive ? "Đang hoạt động" : "Ngừng hoạt động"}</td>
                <td style={{ display: "flex", justifyContent: "space-around" }}>
                  <Button variant="secondary" size="sm" onClick={() => handleFetchOneQuiz(quiz.quizId)}>
                    Chi tiết
                  </Button>
                  <Button variant="warning" size="sm" className="me-2" onClick={() => onEdit(true, quiz)}>
                    Sửa
                  </Button>
                  <Button variant="danger" size="sm" className="me-2" onClick={() => onDelete(true, quiz.quizId)}>
                    Xóa
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="8" className="text-center">
                Không có bài quiz nào.
              </td>
            </tr>
          )}
        </tbody>
      </Table>

      <CustomPagination metadata={metadata} onPageChange={onPageChange} />
    </>
  );
};

export default TableQuiz;
