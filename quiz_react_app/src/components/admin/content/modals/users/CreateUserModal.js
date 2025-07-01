import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";

function CreateUserModal() {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  return (
    <>
      <Button variant="primary" onClick={handleShow}>
        Launch demo modal
      </Button>

      <Modal show={show} onHide={handleClose} size="xl" backdrop="static">
        <Modal.Header closeButton>
          <Modal.Title>Add new user</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <form className="row g-3">
            <div className="col-md-6">
              <label className="form-label">Email</label>
              <input type="email" className="form-control" />
            </div>
            <div className="col-md-6">
              <label className="form-label">Password</label>
              <input type="password" className="form-control" />
            </div>
            <div className="col-md-6">
              <label className="form-label">Fullname</label>
              <input type="text" className="form-control" placeholder="User's fullname" />
            </div>
            <div className="col-md-6">
              <label className="form-label">Role</label>
              <select className="form-select">
                <option selected value={"USER"}>
                  USER
                </option>
                <option value={"ADMIN"}>ADMIN</option>
              </select>
            </div>
            <div className="col-12">
              <label className="form-label">Avatar</label>
              <input type="file" className="form-control" placeholder="Apartment, studio, or floor" />
            </div>
          </form>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleClose}>
            Close
          </Button>
          <Button variant="primary" onClick={handleClose}>
            Save
          </Button>
        </Modal.Footer>
      </Modal>
    </>
  );
}
export default CreateUserModal;
