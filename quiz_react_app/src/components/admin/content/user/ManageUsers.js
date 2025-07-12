import CreateUserModal from "../modals/users/CreateUserModal";
import { FaUserPlus } from "react-icons/fa";
import "./ManageUser.scss";
import { useEffect, useState } from "react";
import TableUser from "./TableUser";
import { getAllUsersService } from "../../../../services/UserServices";
import UpdateUserModal from "../modals/users/UpdateUserModal";
import DeleteUserModal from "../modals/users/DeleteUserModal";
import DetailUserModal from "../modals/users/DetailUserModal";
import FormUserFilter from "./FormUserFilter";
const ManageUsers = () => {
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
  };
  const handleUpdateUser = (showValue, userData) => {
    setShowModalUpdateUser(showValue);
    setUserData(userData);
  };
  const handleDeleteUser = (showValue, userId) => {
    setShowModalDeleteUser(showValue);
    setDeleteUserId(userId);
  };

  const fetchData = async (pageNumber, filter) => {
    try {
      const response = await getAllUsersService(pageNumber, filter);
      // phải có await thì mới gọi ra data như interceptor đã cấu hình
      if (response.statusCode === 200) {
        setListUsers(response.data.users);
        setMetadata(response.data.metadata);
      }
    } catch (error) {
      console.error("❌ Lỗi khi gọi API:", error);
    }
  };
  useEffect(() => {
    fetchData(1, filter); // Load dữ liệu ban đầu với filter mặc định
  }, []); // Chỉ chạy 1 lần khi mount
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
        <FormUserFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
      </div>
      <div className="users-content">
        <div className="table-users-container">
          <TableUser users={listUsers} metadata={metadata} onEdit={handleUpdateUser} onDelete={handleDeleteUser} onDetail={handleDetailUser} fetchUsers={fetchData} filter={filter} />
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
