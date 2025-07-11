import CreateQuizModal from "../modals/quizzes/CreateQuizModal";
import { FaPlus } from "react-icons/fa";
import "./ManageQuizzes.scss";
import { useEffect, useState } from "react";
import TableQuizzes from "./TableQuizzes";
import { getAllQuizzesService } from "../../../../services/QuizServices";
import UpdateQuizModal from "../modals/quizzes/UpdateQuizModal";
import DeleteQuizModal from "../modals/quizzes/DeleteQuizModal";
import DetailQuizModal from "../modals/quizzes/DetailQuizModal";
import FormQuizFilter from "./FormQuizFilter";

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
        <FormQuizFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
      </div>

      <div className="quizzes-content">
        <div className="table-quizzes-container">
          <TableQuizzes quizzes={listQuizzes} metadata={metadata} onEdit={handleUpdateQuiz} onDelete={handleDeleteQuiz} onDetail={handleDetailQuiz} fetchQuizzes={fetchData} filter={filter} />
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
