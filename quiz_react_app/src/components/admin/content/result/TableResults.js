import { Table, Button } from "react-bootstrap";
import AdvancedPagination from "../AdvancedPagination";

const TableResults = ({ fetchResults, results, metadata, onDelete, onDetail, filter }) => {
  const onPageChange = (pageNumber, filter) => {
    if (pageNumber === metadata.currentPage) return;
    fetchResults(pageNumber, filter);
  };

  return (
    <>
      <Table striped bordered hover responsive className="mt-3" style={{ minHeight: "250px" }}>
        <thead className="table-dark">
          <tr>
            <th>STT</th>
            <th>ID Kết quả</th>
            <th>Người làm</th>
            <th>Bài quiz</th>
            <th>Điểm</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {results && results.length > 0 ? (
            results.map((result, index) => (
              <tr key={result.id}>
                <td>{index + 1 + (metadata.currentPage - 1) * metadata.pageSize}</td>
                <td>{result.id}</td>
                <td>User #{result.userId}</td>
                <td>Quiz #{result.quizId}</td>
                <td>
                  {result.score}/{result.totalQuestions || "N/A"}
                </td>
                <td style={{ display: "flex", justifyContent: "space-around" }}>
                  <Button variant="info" size="sm" onClick={() => onDetail(true, result)} className="me-2">
                    Chi tiết
                  </Button>
                  <Button variant="danger" size="sm" onClick={() => onDelete(true, result)}>
                    Xóa
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="6" className="text-center">
                Không có kết quả nào.
              </td>
            </tr>
          )}
        </tbody>
      </Table>

      <AdvancedPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
    </>
  );
};

export default TableResults;
