import CreateUserModal from "./modals/users/CreateUserModal";
import { FaUserPlus } from "react-icons/fa";
import "./ManageUser.scss";
import { useEffect, useState } from "react";
import TableUser from "./TableUser";
import { getAllUsersService } from "../../../services/UserServices";
import UpdateUserModal from "./modals/users/UpdateUserModal";
import DeleteUserModal from "./modals/users/DeleteUserModal";
import DetailUserModal from "./modals/users/DetailUserModal";
const ManageUsers = (props) => {
  const [listUsers, setListUsers] = useState([]);
  const [showModalCreateUser, setShowModalCreateUser] = useState(false);
  const [showModalUpdateUser, setShowModalUpdateUser] = useState(false);
  const [showModalDeleteUser, setShowModalDeleteUser] = useState(false);
  const [showModalDetailUser, setShowModalDetailUser] = useState(false);
  const [userData, setUserData] = useState({});
  const [deleteUserId, setDeleteUserId] = useState(null);
  const [detailUserData, setDetailUserData] = useState(null);
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

  const fetchData = async () => {
    try {
      const response = await getAllUsersService();
      // phải có await thì mới gọi ra data như interceptor đã cấu hình
      console.log("✅ Response từ API:", response);
      if (response.statusCode === 200) {
        setListUsers(response.data);
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
      <div className="users-content">
        <div className="table-users-container">
          <TableUser users={listUsers} onEdit={handleUpdateUser} onDelete={handleDeleteUser} onDetail={handleDetailUser} />
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
