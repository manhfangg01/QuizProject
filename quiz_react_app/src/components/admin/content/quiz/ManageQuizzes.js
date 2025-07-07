import CreateQuizModal from "../modals/quizzes/CreateQuizModal";
import { FaPlus } from "react-icons/fa";
import "./ManageQuizzes.scss";
import { useEffect, useState } from "react";
import TableQuizzes from "./TableQuizzes";
import { getAllQuizzesService } from "../../../../services/QuizServices";
import UpdateQuizModal from "../modals/quizzes/UpdateQuizModal";
import DeleteQuizModal from "../modals/quizzes/DeleteQuizModal";
import DetailQuizModal from "../modals/quizzes/DetailQuizModal";
import { Button, Form } from "react-bootstrap";

const ManageQuiz = () => {
  const [metadata, setMetadata] = useState({});
  const [listQuizzes, setListQuizzes] = useState([]);
  const [showModalCreateQuiz, setShowModalCreateQuiz] = useState(false);
  const [showModalUpdateQuiz, setShowModalUpdateQuiz] = useState(false);
  const [showModalDeleteQuiz, setShowModalDeleteQuiz] = useState(false);
  const [showModalDetailQuiz, setShowModalDetailQuiz] = useState(false);
  const [quizData, setQuizData] = useState({});
  const [deleteQuizId, setDeleteQuizId] = useState(null);
  const [detailQuizData, setDetailQuizData] = useState(null);

  const [filter, setFilter] = useState({
    id: "",
    title: "",
    subject: "",
    difficulty: "",
    active: "",
    totalParticipants: "",
    timeLimit: "",
  });

  const handleSearch = () => {
    fetchData(1, filter);
  };

  const handleClear = () => {
    const reset = { id: "", title: "", subject: "", difficulty: "", active: "", totalParticipants: "", timeLimit: "" };
    setFilter(reset);
    fetchData(1, reset);
  };

  const handleShowHideCreateQuizModal = (value) => {
    setShowModalCreateQuiz(value);
  };

  const handleDetailQuiz = (showValue, quizData) => {
    setShowModalDetailQuiz(showValue);
    setDetailQuizData(quizData);
  };

  const handleUpdateQuiz = (showValue, quizData) => {
    setShowModalUpdateQuiz(showValue);
    setQuizData(quizData);
  };

  const handleDeleteQuiz = (showValue, quizId) => {
    setShowModalDeleteQuiz(showValue);
    setDeleteQuizId(quizId);
  };

  const fetchData = async (pageNumber = 1, filterParam = {}) => {
    try {
      const response = await getAllQuizzesService(pageNumber, filterParam);
      console.log("✅ Quizzes fetched:", response);
      if (response.statusCode === 200) {
        setListQuizzes(response.data.quizzes);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Error fetching quizzes:", error);
    }
  };

  useEffect(() => {
    fetchData();
  }, []);

  return (
    <div className="manage-quizzes-container">
      <div className="title-and-btn">
        <div className="title">Manage Quiz</div>
        <div className="btn-add-new">
          <button className="btn btn-primary" onClick={() => setShowModalCreateQuiz(true)}>
            <FaPlus /> Add new quiz
          </button>
        </div>
      </div>

      <div className="filter-manager mb-3">
        <Form className="row g-2 align-items-end">
          <div className="col-md-2">
            <Form.Label>ID</Form.Label>
            <Form.Control type="text" placeholder="Search by ID" value={filter.id} onChange={(e) => setFilter({ ...filter, id: e.target.value })} />
          </div>
          <div className="col-md-3">
            <Form.Label>Title</Form.Label>
            <Form.Control type="text" placeholder="Search by Title" value={filter.title} onChange={(e) => setFilter({ ...filter, title: e.target.value })} />
          </div>
          <div className="col-md-3">
            <Form.Label>Subject</Form.Label>
            <Form.Control type="text" placeholder="Search by Subject" value={filter.subject} onChange={(e) => setFilter({ ...filter, subject: e.target.value })} />
          </div>
          <div className="col-md-2">
            <Form.Label>Level</Form.Label>
            <Form.Select value={filter.difficulty} onChange={(e) => setFilter({ ...filter, difficulty: e.target.value })}>
              <option value="">-- All --</option>
              <option value="EASY">-- Easy --</option>
              <option value="MEDIUM">-- Medium --</option>
              <option value="HARD">-- Hard --</option>
            </Form.Select>
          </div>
          <div className="col-md-2">
            <Form.Label>Level</Form.Label>
            <Form.Select value={filter.active} onChange={(e) => setFilter({ ...filter, active: e.target.value })}>
              <option value="">-- None --</option>
              <option value="TRUE">-- Active --</option>
              <option value="FALSE">-- InActive --</option>
            </Form.Select>
          </div>
          <div className="d-flex justify-content-center gap-5">
            <div className="col-md-3">
              <Form.Label>TotalParticipants</Form.Label>
              <Form.Control type="text" placeholder="Search by TotalParticipants" value={filter.totalParticipants} onChange={(e) => setFilter({ ...filter, totalParticipants: e.target.value })} />
            </div>

            <div className="col-md-2">
              <Form.Label>TimeLimit</Form.Label>
              <Form.Select value={filter.timeLimit} onChange={(e) => setFilter({ ...filter, timeLimit: e.target.value })}>
                <option value="">-- None --</option>
                <option value="30">-- 30 Phút --</option>
                <option value="45">-- 45 Phút --</option>
                <option value="60">-- 60 Phút --</option>
                <option value="90">-- 90 Phút --</option>
              </Form.Select>
            </div>
          </div>

          <div className="d-flex justify-content-center gap-5">
            <div className="col-md-2 d-flex">
              <Button variant="primary" className="me-2" onClick={handleSearch}>
                Search
              </Button>
              <Button variant="secondary" onClick={handleClear}>
                Clear
              </Button>
            </div>
          </div>
        </Form>
      </div>

      <div className="quizzes-content">
        <div className="table-quizzes-container">
          <TableQuizzes quizzes={listQuizzes} metadata={metadata} onEdit={handleUpdateQuiz} onDelete={handleDeleteQuiz} onDetail={handleDetailQuiz} fetchQuizzes={fetchData} />
        </div>

        <CreateQuizModal show={showModalCreateQuiz} setShow={handleShowHideCreateQuizModal} onCreateQuiz={fetchData} />
        <UpdateQuizModal show={showModalUpdateQuiz} setShow={handleUpdateQuiz} onUpdateQuiz={fetchData} quizData={quizData} />
        <DeleteQuizModal show={showModalDeleteQuiz} setShow={handleDeleteQuiz} onDeleteQuiz={fetchData} quizId={deleteQuizId} />
        <DetailQuizModal show={showModalDetailQuiz} setShow={handleDetailQuiz} quizData={detailQuizData} />
      </div>
    </div>
  );
};

export default ManageQuiz;
