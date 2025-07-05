import CreateUserModal from "./modals/users/CreateUserModal";
import { FaUserPlus } from "react-icons/fa";
import "./ManageUser.scss";
import { useEffect, useState } from "react";
import TableUser from "./TableUser";
import { getAllUsersService } from "../../../services/UserServices";
import UpdateUserModal from "./modals/users/UpdateUserModal";
const ManageUsers = (props) => {
  const [listUsers, setListUsers] = useState([]);
  const [showModalCreateUser, setShowModalCreateUser] = useState(false);
  const [showModalUpdateUser, setShowModalUpdateUser] = useState(false);
  const [userData, setUserData] = useState({});
  const handleShowHideCreateUserModal = (value) => {
    setShowModalCreateUser(value);
  };
  const handleUpdateUser = (showValue, userData) => {
    setShowModalUpdateUser(showValue);
    setUserData(userData);
    console.log("Debugg", userData);
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
          <TableUser users={listUsers} onEdit={handleUpdateUser} />
        </div>
        <CreateUserModal show={showModalCreateUser} setShow={handleShowHideCreateUserModal} onCreateUser={fetchData} />
        <UpdateUserModal show={showModalUpdateUser} setShow={handleUpdateUser} onUpdateUser={fetchData} userData={userData} />
      </div>
    </div>
  );
};
export default ManageUsers;
