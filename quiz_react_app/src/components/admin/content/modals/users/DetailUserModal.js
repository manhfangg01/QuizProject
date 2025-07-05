import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";

const DetailUserModal = ({ show, setShow, userData }) => {
  const handleClose = () => {
    setShow(false);
  };

  return (
    <Modal show={show} onHide={handleClose} backdrop="static" size="md">
      <Modal.Header closeButton>
        <Modal.Title>Chi tiết người dùng</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        {userData ? (
          <div>
            <div className="mb-3">
              <strong>Email:</strong> {userData.email}
            </div>
            <div className="mb-3">
              <strong>Họ và tên:</strong> {userData.fullName}
            </div>
            <div className="mb-3">
              <strong>Vai trò:</strong> {userData.role}
            </div>
            <div className="mb-3">
              <strong>Ảnh đại diện:</strong>
              <div>
                {userData.userAvatarUrls ? (
                  <img src={userData.userAvatarUrls} alt="Avatar" className="img-thumbnail mt-2" style={{ maxWidth: "200px", maxHeight: "200px" }} />
                ) : (
                  <div className="text-muted mt-2">Không có ảnh</div>
                )}
              </div>
            </div>
          </div>
        ) : (
          <div className="text-muted">Không có dữ liệu người dùng.</div>
        )}
      </Modal.Body>
      <Modal.Footer>
        <Button variant="secondary" onClick={handleClose}>
          Đóng
        </Button>
      </Modal.Footer>
    </Modal>
  );
};

export default DetailUserModal;
