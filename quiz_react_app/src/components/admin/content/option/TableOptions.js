import { Table, Button, Badge } from "react-bootstrap";
import { getOptionById } from "../../../../services/OptionService";
import { Bounce, toast } from "react-toastify";
import CustomPagination from "../CustomPagination";

const TableOptions = ({ fetchOptions, options, metadata, onEdit, onDelete, onDetail, filter }) => {
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

  const handleFetchOneOption = async (optionId) => {
    if (!optionId) onDetail(true, null);

    try {
      const res = await getOptionById(optionId);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        onDetail(true, res.data);
      } else {
        showToast("warning", res?.message || "Không thể hiển thị lựa chọn.");
      }
    } catch (err) {
      console.error("Error getting option:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi!");
    }
  };

  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    fetchOptions(pageNumber);
  };

  // Function to truncate long text
  const truncateText = (text, maxLength = 30) => {
    if (text.length <= maxLength) return text;
    return `${text.substring(0, maxLength)}...`;
  };

  return (
    <>
      <Table striped bordered hover responsive className="mt-3" style={{ minHeight: "280px" }}>
        <thead className="table-dark">
          <tr>
            <th>STT</th>
            <th>Nội dung</th>
            <th>Đúng/Sai</th>
            <th>Thao tác</th>
          </tr>
        </thead>
        <tbody>
          {options && options.length > 0 ? (
            options.map((option, index) => (
              <tr key={option.id}>
                <td>{option.id}</td>
                <td>{truncateText(option.context)}</td>
                <td>
                  <Badge bg={option.isCorrect ? "success" : "danger"}>{option.isCorrect ? "Đúng" : "Sai"}</Badge>
                </td>
                <td style={{ display: "flex", justifyContent: "space-around" }}>
                  <Button variant="secondary" size="sm" onClick={() => handleFetchOneOption(option.id)}>
                    Chi tiết
                  </Button>
                  <Button variant="warning" size="sm" className="me-2" onClick={() => onEdit(true, option)}>
                    Sửa
                  </Button>
                  <Button variant="danger" size="sm" className="me-2" onClick={() => onDelete(true, option.id)}>
                    Xóa
                  </Button>
                </td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="4" className="text-center">
                Không có lựa chọn nào.
              </td>
            </tr>
          )}
        </tbody>
      </Table>
      <CustomPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
    </>
  );
};

export default TableOptions;
