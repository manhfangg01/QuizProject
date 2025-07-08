import CreateOptionModal from "../modals/options/CreateOptionModal";
import UpdateOptionModal from "../modals/options/UpdateOptionModal";
import DetailOptionModal from "../modals/options/DetailOptionModal";
import DeleteOptionModal from "../modals/options/DeleteOptionModal";
import TableOptions from "./TableOptions";
import FormOptionFilter from "./FormOptionFilter";
import { FaUserPlus } from "react-icons/fa";
import { useEffect, useState } from "react";
import { getAllOptions } from "../../../../services/OptionService";

import "./ManageOptions.scss";

const ManageOptions = () => {
  const [metadata, setMetadata] = useState({});
  const [listOptions, setListOptions] = useState([]);
  const [showModalCreateOption, setShowModalCreateOption] = useState(false);
  const [showModalUpdateOption, setShowModalUpdateOption] = useState(false);
  const [showModalDeleteOption, setShowModalDeleteOption] = useState(false);
  const [showModalDetailOption, setShowModalDetailOption] = useState(false);
  const [optionData, setOptionData] = useState({});
  const [deleteOptionId, setDeleteOptionId] = useState(null);
  const [detailOptionData, setDetailOptionData] = useState(null);
  const [filter, setFilter] = useState({
    id: "",
    context: "",
    isCorrect: "",
    questionId: "",
  });
  const fetchOptions = async (pageNumber, filter) => {
    try {
      const response = await getAllOptions(pageNumber, filter);
      if (response.statusCode === 200) {
        setListOptions(response.data.options);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
    }
  };

  useEffect(() => {
    fetchOptions(1, filter); // Load dữ liệu ban đầu với filter mặc định
  }, []);

  const handleSearch = () => {
    fetchOptions(1, filter); // truyền filter xuống backend
  };

  const handleClear = () => {
    const reset = { id: "", context: "", isCorrect: "", questionId: "" };
    setFilter(reset);
    fetchOptions(1, reset);
  };

  const handleShowHideCreateOptionModal = (value) => {
    setShowModalCreateOption(value);
  };

  const handleDetailOption = (showValue, optionData) => {
    setShowModalDetailOption(showValue);
    setDetailOptionData(optionData);
  };

  const handleUpdateOption = (showValue, optionData) => {
    setShowModalUpdateOption(showValue);
    setOptionData(optionData);
  };

  const handleDeleteOption = (showValue, optionId) => {
    setShowModalDeleteOption(showValue);
    setDeleteOptionId(optionId);
  };

  return (
    <>
      <div className="manage-options-container">
        <div className="title-and-btn">
          <div className="title">Manage Options</div>
          <div className="btn-add-new">
            <button
              className="btn btn-primary"
              onClick={() => {
                setShowModalCreateOption(true);
              }}
            >
              <FaUserPlus /> Add new option
            </button>
          </div>
        </div>
        <div className="filter-manager mb-3">
          <FormOptionFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
        </div>
        <div className="options-content">
          <div className="table-options-container">
            <TableOptions
              options={listOptions}
              metadata={metadata}
              onEdit={handleUpdateOption}
              onDelete={handleDeleteOption}
              onDetail={handleDetailOption}
              fetchOptions={fetchOptions}
              filter={filter}
            />
          </div>
          <CreateOptionModal show={showModalCreateOption} setShow={handleShowHideCreateOptionModal} onCreateOption={fetchOptions} />
          <UpdateOptionModal show={showModalUpdateOption} setShow={handleUpdateOption} onUpdateOption={fetchOptions} optionData={optionData} />
          <DeleteOptionModal show={showModalDeleteOption} setShow={handleDeleteOption} onDeleteOption={fetchOptions} optionId={deleteOptionId} />
          <DetailOptionModal show={showModalDetailOption} setShow={handleDetailOption} optionData={detailOptionData} />
        </div>
      </div>
    </>
  );
};

export default ManageOptions;
