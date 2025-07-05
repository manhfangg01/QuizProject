import { Table, Button } from "react-bootstrap";
const TableUser = ({ users, onEdit, onDelete, onDetail }) => {
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
                <Button variant="warning" size="sm" className="me-2" onClick={() => onEdit(true, user)}>
                  Sửa
                </Button>
                <Button variant="danger" size="sm" className="me-2" onClick={() => onDelete(user.id)}>
                  Xóa
                </Button>
                <Button variant="secondary" size="sm" onClick={() => onDelete(user.id)}>
                  Chi tiết
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
