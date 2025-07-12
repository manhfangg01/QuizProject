import { useEffect, useState } from "react";
import { getAllQuizzes } from "../../../../services/QuizServices";
import { toast } from "react-toastify";
import { FaUserPlus } from "react-icons/fa";
import FormQuizFilter from "./FormQuizFilter";
import CreateQuizModal from "../modals/quizzes/CreateQuizModal";
import { getAllQuestions } from "../../../../services/QuestionServices";
import TableQuizzes from "./TableQuizzes";
import UpdateQuizModal from "../modals/quizzes/UpdateQuizModal";
import UpdateQuestionModalForQuiz from "../modals/quizzes/UpdateQuestionsModalForQuiz";
import SelectQuestionsModalForQuiz from "../modals/quizzes/SelectQuestionsModalForQuiz";
import DeleteQuizModal from "../modals/quizzes/DeleteQuizModal";
import DetailQuizModal from "../modals/quizzes/DetailQuizModal";
import "./ManageQuizzes.scss";

const ManageQuizzes = () => {
  const [metadata, setMetadata] = useState({});
  const [listQuizzes, setListQUizzes] = useState([]);
  const [questions, setQuestions] = useState([]);
  const [metadataForQuestion, setMetadataForQuestion] = useState({});

  const [filter, setFilter] = useState({
    id: "",
    title: "",
    subject: "",
    difficulty: "",
    active: "",
    totalParticipants: "",
    timeLimit: "",
  });
  const [showModalCreateQuiz, setShowModalCreateQuiz] = useState(false);
  const [showModalUpdateQuiz, setShowModalUpdateQuiz] = useState(false);
  const [showModalDeleteQuiz, setShowModalDeleteQuiz] = useState(false);
  const [showModalDetailQuiz, setShowModalDetailQuiz] = useState(false);
  const [showModalSelectQuestion, setShowModalSelectQuestion] = useState(false);
  const [showModalUpdateQuestion, setShowModalUpdateQuestion] = useState(false);
  const [selectedQuestionIds, setSelectedQuestionIds] = useState([]);
  const [updatedQuestionIds, setUpdatedQuestionIds] = useState([]);

  const [quizData, setQuizData] = useState({});

  const fetchAllQuizzes = async (pageNumber, filter) => {
    try {
      const response = await getAllQuizzes(pageNumber, filter);
      if (response.statusCode === 200) {
        setListQUizzes(response.data.quizzes);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      toast.warning(error);
    }
  };

  const fetchAllQuestions = async (pageNumber, filter) => {
    try {
      const response = await getAllQuestions(pageNumber, filter);
      if (response.statusCode === 200) {
        setQuestions(response.data.questions);
        setMetadataForQuestion(response.data.metadata);
      }
    } catch (error) {
      toast.warning("❌ Error calling API:", error);
    }
  };

  useEffect(() => {
    fetchAllQuizzes(1, filter);
  }, []);

  const handleCreateQuiz = () => {
    fetchAllQuizzes(1, {});
  };

  const handleDetailQuiz = (showValue, quizData) => {
    setShowModalDetailQuiz(showValue);
    setQuizData(quizData);
  };

  const handleUpdateQuiz = (showValue, quizData) => {
    setShowModalUpdateQuiz(showValue);
    setQuizData(quizData);
  };

  const handleDeleteQuiz = (showValue, quizData) => {
    setShowModalDeleteQuiz(showValue);
    setQuizData(quizData);
  };

  return (
    <div className="manage-quizzes-container">
      <div className="title-and-btn">
        <div className="title">Manage Quizzes</div>
        <div className="btn-add-new">
          <button className="btn btn-primary" onClick={() => setShowModalCreateQuiz(true)}>
            <FaUserPlus /> Add new quiz
          </button>
        </div>
      </div>
      <div className="filter-manager mb-3">
        <FormQuizFilter filter={filter} setFilter={setFilter} fetchAllQuizzes={fetchAllQuizzes} />
      </div>
      <div className="quizzes-content">
        <div className="table-quizzes-container">
          <TableQuizzes quizzes={listQuizzes} metadata={metadata} onEdit={handleUpdateQuiz} onDelete={handleDeleteQuiz} onDetail={handleDetailQuiz} fetchQuizzes={fetchAllQuizzes} filter={filter} />
        </div>

        <SelectQuestionsModalForQuiz
          show={showModalSelectQuestion}
          setShow={setShowModalSelectQuestion}
          questions={questions}
          selectedQuestionIds={selectedQuestionIds || []}
          setSelectedQuestionIds={setSelectedQuestionIds}
          metadata={metadataForQuestion}
          fetchAllQuestions={fetchAllQuestions}
        />
        <CreateQuizModal
          show={showModalCreateQuiz}
          setShow={setShowModalCreateQuiz}
          onCreateQuiz={handleCreateQuiz}
          setShowQuestionSelection={setShowModalSelectQuestion}
          setQuestions={setQuestions}
          fetchAllQuestions={fetchAllQuestions}
          selectedQuestionIds={selectedQuestionIds}
          setSelectedQuestionIds={setSelectedQuestionIds}
        />
        <UpdateQuizModal
          show={showModalUpdateQuiz}
          setShow={setShowModalUpdateQuiz}
          onUpdateQuiz={fetchAllQuizzes}
          quizData={quizData}
          setShowUpdateQuestions={setShowModalUpdateQuestion}
          updatedQuestionIds={updatedQuestionIds}
          setUpdatedQuestionIds={setUpdatedQuestionIds}
          fetchAllQuestions={fetchAllQuestions}
        />

        <UpdateQuestionModalForQuiz
          show={showModalUpdateQuestion}
          setShow={setShowModalUpdateQuestion}
          quizData={quizData}
          questions={questions}
          updatedQuestionIds={updatedQuestionIds}
          setUpdatedQuestionIds={setUpdatedQuestionIds}
          fetchAllQuestions={fetchAllQuestions}
        />

        <DeleteQuizModal show={showModalDeleteQuiz} setShow={setShowModalDeleteQuiz} onDeleteQuiz={fetchAllQuizzes} quizData={quizData} />
        <DetailQuizModal show={showModalDetailQuiz} setShow={setShowModalDetailQuiz} quizData={quizData} />
      </div>
    </div>
  );
};

export default ManageQuizzes;
