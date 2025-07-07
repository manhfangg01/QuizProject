import { Button, Form } from "react-bootstrap";

const FormUserFilter = ({ filter, setFilter, handleSearch, handleClear }) => {
  return (
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
  );
};
export default FormUserFilter;
