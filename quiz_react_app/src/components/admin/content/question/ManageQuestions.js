import CreateQuestionModal from "../modals/questions/CreateQuestionModal";
import UpdateQuestionModal from "../modals/questions/UpdateQuestionModal";
import DetailQuestionModal from "../modals/questions/DetailQuestionModal";
import DeleteQuestionModal from "../modals/questions/DeleteQuestionModal";
import TableQuestions from "./TableQuestions";
import FormQuestionFilter from "./FormQuestionFilter";
import { FaUserPlus } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getAllQuestions, postCreateQuestion } from "../../../../services/QuestionServices";
import { getAllAvailableOptions } from "../../../../services/OptionService";
import "./ManageQuestions.scss";
import { toast } from "react-toastify";
import SelectOptionModal from "../modals/questions/SelectOptionModal";
import UpdateOptionsForQuestionModal from "../modals/questions/UpdateOptionsForQuestionModal";

const ManageQuestions = () => {
  const [metadata, setMetadata] = useState({});
  const [listQuestions, setListQuestions] = useState([]);
  const [showModalCreateQuestion, setShowModalCreateQuestion] = useState(false);
  const [showModalUpdateQuestion, setShowModalUpdateQuestion] = useState(false);
  const [showModalDeleteQuestion, setShowModalDeleteQuestion] = useState(false);
  const [showModalDetailQuestion, setShowModalDetailQuestion] = useState(false);
  const [questionData, setQuestionData] = useState({});
  const [deleteQuestionId, setDeleteQuestionId] = useState(null);
  const [detailQuestionData, setDetailQuestionData] = useState(null);
  const [filter, setFilter] = useState({
    id: "",
    context: "",
    numberOfOptions: "",
    quizId: "",
  });
  const [availableOptions, setAvailableOptions] = useState({});
  const [selectedOptionIds, setSelectedOptionIds] = useState({});
  const [showSelectOptionModal, setShowSelectOptionModal] = useState(false);
  const [metadataForOptions, setMetadataForOptions] = useState({});
  const [showUpdateOptionsForQuestionModal, setShowUpdateOptionsForQuestionModal] = useState(false);

  const [updateModalOptionIds, setUpdateModalOptionIds] = useState([]);

  const fetchAvailableOptions = async (pageNumber, filter) => {
    try {
      const response = await getAllAvailableOptions(pageNumber, filter);
      if (response.statusCode === 200) {
        setAvailableOptions(response.data.options);
        setMetadataForOptions(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Error calling API:", error);
    }
  };
  const fetchQuestions = async (pageNumber, filter) => {
    try {
      const response = await getAllQuestions(pageNumber, filter);
      if (response.statusCode === 200) {
        setListQuestions(response.data.questions);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Error calling API:", error);
    }
  };

  const handleCreateQuestion = async (questionData) => {
    try {
      const res = await postCreateQuestion(questionData.context, questionData.optionIds);
      if (res.statusCode === 200 || res.statusCode === 201) {
        toast.success("Question created successfully!");
        fetchQuestions(1, filter);
        return true;
      } else {
        toast.error(res.error);
        fetchQuestions(1, filter);
        return false;
      }
    } catch (error) {
      toast.error("Error creating question!");
      console.error("Error:", error);
    }
  };

  useEffect(() => {
    fetchQuestions(1, filter);
    fetchAvailableOptions(1, {});
  }, []);

  const handleSearch = () => {
    fetchQuestions(1, filter);
  };

  const handleClear = () => {
    const reset = { id: "", context: "", numberOfOptions: "", quizId: "" };
    setFilter(reset);
    fetchQuestions(1, reset);
  };

  const handleDetailQuestion = (showValue, questionData) => {
    setShowModalDetailQuestion(showValue);
    setDetailQuestionData(questionData);
  };

  const handleUpdateQuestion = (showValue, questionData) => {
    setShowModalUpdateQuestion(showValue);
    setQuestionData(questionData);
  };

  const handleDeleteQuestion = (showValue, questionId) => {
    setShowModalDeleteQuestion(showValue);
    setDeleteQuestionId(questionId);
  };

  return (
    <div className="manage-questions-container">
      <div className="title-and-btn">
        <div className="title">Manage Questions</div>
        <div className="btn-add-new">
          <button className="btn btn-primary" onClick={() => setShowModalCreateQuestion(true)}>
            <FaUserPlus /> Add new question
          </button>
        </div>
      </div>
      <div className="filter-manager mb-3">
        <FormQuestionFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
      </div>
      <div className="questions-content">
        <div className="table-questions-container">
          <TableQuestions
            questions={listQuestions}
            metadata={metadata}
            onEdit={handleUpdateQuestion}
            onDelete={handleDeleteQuestion}
            onDetail={handleDetailQuestion}
            fetchQuestions={fetchQuestions}
            filter={filter}
          />
        </div>

        <SelectOptionModal
          show={showSelectOptionModal}
          setShow={setShowSelectOptionModal}
          availableOptions={availableOptions}
          selectedOptionIds={selectedOptionIds || []}
          setSelectedOptionIds={setSelectedOptionIds}
          metadata={metadataForOptions}
          fetchOptions={fetchAvailableOptions}
        />
        <CreateQuestionModal
          show={showModalCreateQuestion}
          setShow={setShowModalCreateQuestion}
          onCreateQuestion={handleCreateQuestion}
          showOptionSelection={showSelectOptionModal}
          setShowOptionSelection={setShowSelectOptionModal}
          selectedOptionIds={selectedOptionIds || []}
          setSelectedOptionIds={setSelectedOptionIds}
        />
        <UpdateQuestionModal
          show={showModalUpdateQuestion}
          setShow={setShowModalUpdateQuestion}
          onUpdateQuestion={fetchQuestions}
          questionData={questionData}
          setShowUpdateOptions={setShowUpdateOptionsForQuestionModal}
          selectedOptionIds={updateModalOptionIds}
          setSelectedOptionIds={setUpdateModalOptionIds}
        />

        <UpdateOptionsForQuestionModal
          show={showUpdateOptionsForQuestionModal}
          setShow={setShowUpdateOptionsForQuestionModal}
          selectedOptionIds={updateModalOptionIds}
          setSelectedOptionIds={setUpdateModalOptionIds}
          questionData={questionData}
        />

        <DeleteQuestionModal show={showModalDeleteQuestion} setShow={handleDeleteQuestion} onDeleteQuestion={fetchQuestions} questionId={deleteQuestionId} />
        <DetailQuestionModal show={showModalDetailQuestion} setShow={handleDetailQuestion} questionData={detailQuestionData} />
      </div>
    </div>
  );
};

export default ManageQuestions;
