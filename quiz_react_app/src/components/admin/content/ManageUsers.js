import CreateUserModal from "./modals/users/CreateUserModal";
import "./ManageUser.scss";
const ManageUsers = (props) => {
  return (
    <div className="manage-users-container">
      <div className="title">Manage User</div>
      <div className="users-content">
        <div>
          <button> Add new user</button>
        </div>
        <div>table users</div>
        <CreateUserModal />
      </div>
    </div>
  );
};
export default ManageUsers;
