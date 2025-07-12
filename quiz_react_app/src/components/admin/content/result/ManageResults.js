import { useEffect, useState } from "react";
import { getAllResults } from "../../../../services/ResultServices";
import FormResultFilter from "./FormResultFilter"; // Import component filter mới
import TableResults from "./TableResults"; // Component hiển thị bảng kết quả
import DeleteResultModal from "../modals/results/DeleteResultModal"; // Modal xóa kết quả
import DetailResultModal from "../modals/results/DetailResultModal"; // Modal chi tiết kết quả

const ManageResults = () => {
  const [metadata, setMetadata] = useState({});
  const [listResults, setListResults] = useState([]);
  const [showModalDeleteResult, setShowModalDeleteResult] = useState(false);
  const [showModalDetailResult, setShowModalDetailResult] = useState(false);
  const [resultData, setResultData] = useState({});

  const [resultFilter, setResultFilter] = useState({
    id: "",
    userId: "",
    quizId: "",
    score: "",
    totalCorrects: "",
    duration: "",
  });

  const fetchAllResults = async (pageNumber, filter) => {
    try {
      const response = await getAllResults(pageNumber, filter);

      if (response.statusCode === 200) {
        setListResults(response.data.results); // Sửa từ users sang results

        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
    }
  };

  const handleDetailResult = (showValue, data) => {
    setShowModalDetailResult(showValue);
    setResultData(data);
  };

  const handleDeleteResult = (showValue, data) => {
    setShowModalDeleteResult(showValue);
    setResultData(data);
  };
  useEffect(() => {
    fetchAllResults(1, {});
  }, []);

  return (
    <div className="manage-results-container">
      <div className="title">Manage Results</div>

      <div className="filter-manager mb-3">
        <FormResultFilter filter={resultFilter} setFilter={setResultFilter} fetchAllResults={fetchAllResults} />
      </div>

      <div className="results-content">
        <div className="table-results-container">
          <TableResults results={listResults} metadata={metadata} onDelete={handleDeleteResult} onDetail={handleDetailResult} fetchResults={fetchAllResults} filter={resultFilter} />
        </div>

        <DeleteResultModal show={showModalDeleteResult} setShow={setShowModalDeleteResult} onDeleteResult={fetchAllResults} resultData={resultData} />

        <DetailResultModal show={showModalDetailResult} setShow={setShowModalDetailResult} resultData={resultData} />
      </div>
    </div>
  );
};

export default ManageResults;
