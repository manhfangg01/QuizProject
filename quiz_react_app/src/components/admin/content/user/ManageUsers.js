import CreateUserModal from "../modals/users/CreateUserModal";
import { FaUserPlus } from "react-icons/fa";
import "./ManageUser.scss";
import { useEffect, useState } from "react";
import TableUser from "./TableUser";
import { getAllUsersService } from "../../../../services/UserServices";
import UpdateUserModal from "../modals/users/UpdateUserModal";
import DeleteUserModal from "../modals/users/DeleteUserModal";
import DetailUserModal from "../modals/users/DetailUserModal";
import { Button, Form } from "react-bootstrap";
const ManageUsers = (props) => {
  const [metadata, setMetadata] = useState({});
  const [listUsers, setListUsers] = useState([]);
  const [showModalCreateUser, setShowModalCreateUser] = useState(false);
  const [showModalUpdateUser, setShowModalUpdateUser] = useState(false);
  const [showModalDeleteUser, setShowModalDeleteUser] = useState(false);
  const [showModalDetailUser, setShowModalDetailUser] = useState(false);
  const [userData, setUserData] = useState({});
  const [deleteUserId, setDeleteUserId] = useState(null);
  const [detailUserData, setDetailUserData] = useState(null);

  const [filter, setFilter] = useState({
    id: "",
    email: "",
    fullName: "",
    role: "",
  });

  const handleSearch = () => {
    fetchData(1, filter); // truyền filter xuống backend
  };

  const handleClear = () => {
    const reset = { id: "", email: "", fullName: "", role: "" };
    setFilter(reset);
    fetchData(1, reset);
  };
  const handleShowHideCreateUserModal = (value) => {
    setShowModalCreateUser(value);
  };
  const handleDetailUser = (showValue, userData) => {
    setShowModalDetailUser(showValue);
    setDetailUserData(userData);
    console.log("Debugg", userData);
  };
  const handleUpdateUser = (showValue, userData) => {
    setShowModalUpdateUser(showValue);
    setUserData(userData);
    console.log("Debugg", userData);
  };
  const handleDeleteUser = (showValue, userId) => {
    setShowModalDeleteUser(showValue);
    setDeleteUserId(userId);
    console.log("Debugg", userId);
  };

  const fetchData = async (pageNumber, filter) => {
    try {
      const response = await getAllUsersService(pageNumber, filter);
      // phải có await thì mới gọi ra data như interceptor đã cấu hình
      console.log(">>>>✅ Response từ API:", response);
      if (response.statusCode === 200) {
        setListUsers(response.data.users);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
    }
  };
  useEffect(() => {
    fetchData();
  }, []);
  return (
    <div className="manage-users-container">
      <div className="title-and-btn">
        <div className="title">Manage User</div>
        <div className="btn-add-new">
          <button
            className="btn btn-primary"
            onClick={() => {
              setShowModalCreateUser(true);
            }}
          >
            <FaUserPlus /> Add new user
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
            <Form.Label>Email</Form.Label>
            <Form.Control type="text" placeholder="Search by Email" value={filter.email} onChange={(e) => setFilter({ ...filter, email: e.target.value })} />
          </div>
          <div className="col-md-3">
            <Form.Label>Full Name</Form.Label>
            <Form.Control type="text" placeholder="Search by Name" value={filter.fullName} onChange={(e) => setFilter({ ...filter, fullName: e.target.value })} />
          </div>
          <div className="col-md-2">
            <Form.Label>Role</Form.Label>
            <Form.Select value={filter.role} onChange={(e) => setFilter({ ...filter, role: e.target.value })}>
              <option value="">-- All --</option>
              <option value="ADMIN">Admin</option>
              <option value="USER">User</option>
              {/* thêm role khác nếu có */}
            </Form.Select>
          </div>
          <div className="col-md-2 d-flex">
            <Button variant="primary" className="me-2" onClick={handleSearch}>
              Search
            </Button>
            <Button variant="secondary" onClick={handleClear}>
              Clear
            </Button>
          </div>
        </Form>
      </div>
      <div className="users-content">
        <div className="table-users-container">
          <TableUser users={listUsers} metadata={metadata} onEdit={handleUpdateUser} onDelete={handleDeleteUser} onDetail={handleDetailUser} fetchUsers={fetchData} />
        </div>
        <CreateUserModal show={showModalCreateUser} setShow={handleShowHideCreateUserModal} onCreateUser={fetchData} />
        <UpdateUserModal show={showModalUpdateUser} setShow={handleUpdateUser} onUpdateUser={fetchData} userData={userData} />
        <DeleteUserModal show={showModalDeleteUser} setShow={handleDeleteUser} onDeleteUser={fetchData} userId={deleteUserId} />
        <DetailUserModal show={showModalDetailUser} setShow={handleDetailUser} userData={detailUserData} />
      </div>
    </div>
  );
};
export default ManageUsers;
