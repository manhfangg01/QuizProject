import CreateUserModal from "./modals/users/CreateUserModal";
import { FaUserPlus } from "react-icons/fa";
import "./ManageUser.scss";
import { useState } from "react";
const ManageUsers = (props) => {
  const [showModalCreateUser, setShowModalCreateUser] = useState(false);
  const handleShowHideModal = (value) => {
    setShowModalCreateUser(value);
  };
  return (
    <div className="manage-users-container">
      <div className="title">Manage User</div>
      <div className="users-content">
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
        <div className="table-users-container">table users</div>
        <CreateUserModal show={showModalCreateUser} setShow={handleShowHideModal} />
      </div>
    </div>
  );
};
export default ManageUsers;
