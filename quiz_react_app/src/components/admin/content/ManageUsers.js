import CreateUserModal from "./modals/users/CreateUserModal";

const ManageUsers = (props) => {
  return (
    <div className="manage-users-container">
      <div className="title">Manage User</div>
      <div className="users-content">
        <button> Add new user</button>
      </div>
      <div>
        table users
        <CreateUserModal />
      </div>
    </div>
  );
};
export default ManageUsers;
