import { Table, Button } from "react-bootstrap";
import { getUserById } from "../../../services/UserServices";
import { Bounce, toast } from "react-toastify";

const TableUser = ({ users, onEdit, onDelete, onDetail }) => {
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
  const handleFetchOneUser = async (userId) => {
    if (!userId) onDetail(true, null);

    try {
      const res = await getUserById(userId);
      if (res && (res.statusCode === 200 || res.statusCode === 204)) {
        onDetail(true, res.data);
      } else {
        showToast("warning", res?.message || "Không thể hiển thị người dùng.");
      }
    } catch (err) {
      console.error("Error deleting user:", err);
      showToast("error", err?.response?.data?.message || "Đã xảy ra lỗi khi xóa!");
    }
  };

  return (
    <Table striped bordered hover responsive className="mt-3">
      <thead className="table-dark">
        <tr>
          <th>STT</th>
          <th>Họ tên</th>
          <th>Email</th>
          <th>Vai trò</th>
          <th>Ngày tạo</th>
          <th>Thao tác</th>
        </tr>
      </thead>
      <tbody>
        {users && users.length > 0 ? (
          users.map((user) => (
            <tr key={user.id}>
              <td>{user.id}</td>
              <td>{user.fullName}</td>
              <td>{user.email}</td>
              <td>{user.role}</td>
              <td>{new Date(user.createdAt).toLocaleString()}</td>
              <td
                style={{
                  display: "flex",
                  justifyContent: "space-around",
                }}
              >
                <Button variant="secondary" size="sm" onClick={() => handleFetchOneUser(user.id)}>
                  Chi tiết
                </Button>
                <Button variant="warning" size="sm" className="me-2" onClick={() => onEdit(true, user)}>
                  Sửa
                </Button>
                <Button variant="danger" size="sm" className="me-2" onClick={() => onDelete(true, user.id)}>
                  Xóa
                </Button>
              </td>
            </tr>
          ))
        ) : (
          <tr>
            <td colSpan="6" className="text-center">
              Không có người dùng nào.
            </td>
          </tr>
        )}
      </tbody>
    </Table>
  );
};

export default TableUser;
