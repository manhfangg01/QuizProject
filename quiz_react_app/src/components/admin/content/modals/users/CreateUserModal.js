import { useState } from "react";
import Button from "react-bootstrap/Button";
import Modal from "react-bootstrap/Modal";
import { LuImagePlus } from "react-icons/lu";

function CreateUserModal() {
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [fullName, setFullName] = useState("");
  const [role, setRole] = useState("USER");
  const [image, setImage] = useState("");
  const [previewAvatar, setPreviewAvatar] = useState("");

  const handleUploadImage = (event) => {
    if (event.target && event.target.files && event.target.files[0]) {
      setPreviewAvatar(URL.createObjectURL(event.target.files[0]));
      setImage(URL.createObjectURL(event.target.files[0]));
    }
  };

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
              <input
                type="email"
                className="form-control"
                value={email}
                onChange={(event) => {
                  setEmail(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Password</label>
              <input
                type="password"
                className="form-control"
                value={password}
                onChange={(event) => {
                  setPassword(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Fullname</label>
              <input
                type="text"
                className="form-control"
                placeholder="User's fullname"
                value={fullName}
                onChange={(event) => {
                  setFullName(event.target.value);
                }}
              />
            </div>
            <div className="col-md-6">
              <label className="form-label">Role</label>
              <select
                className="form-select"
                onChange={(event) => {
                  setRole(event.target.value);
                }}
              >
                <option value={"USER"}>USER</option>
                <option value={"ADMIN"}>ADMIN</option>
              </select>
            </div>
            <div className="col-12 label-uploading ">
              <label className="form-label btn " htmlFor="lableUploading">
                <LuImagePlus /> Avatar
              </label>
              <input type="file" className="form-control" id="lableUploading" hidden onChange={(event) => handleUploadImage(event)} />
            </div>

            <div className="col-md-12 modal-add-user">{!previewAvatar ? <span>Preview Image</span> : <img src={previewAvatar} alt="User avatar preview" className="img-preview"></img>}</div>
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
