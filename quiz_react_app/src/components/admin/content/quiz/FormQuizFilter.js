import { Button, Form } from "react-bootstrap";
import { toast } from "react-toastify";

const FormQuizFilter = ({ filter, setFilter, fetchAllQuizzes }) => {
  const handleSearch = (pageNumber, filter) => {
    toast.warning("GAY");
    fetchAllQuizzes(pageNumber, filter);
  };

  const handleClear = () => {
    const reset = { id: "", title: "", subject: "", difficulty: "", active: "", totalParticipants: "", timeLimit: "" };
    setFilter(reset);
    fetchAllQuizzes(1, reset);
  };
  return (
    <Form className="row g-2 align-items-end d-flex justify-content-center">
      <div className="col-md-1">
        <Form.Label>ID</Form.Label>
        <Form.Control type="text" placeholder="Search by ID" value={filter.id} onChange={(e) => setFilter({ ...filter, id: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Title</Form.Label>
        <Form.Control type="text" placeholder="Search by Title" value={filter.title} onChange={(e) => setFilter({ ...filter, title: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Subject</Form.Label>
        <Form.Control type="text" placeholder="Search by Subject" value={filter.subject} onChange={(e) => setFilter({ ...filter, subject: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Level</Form.Label>
        <Form.Select value={filter.difficulty} onChange={(e) => setFilter({ ...filter, difficulty: e.target.value })}>
          <option value=""> All </option>
          <option value="EASY">-- Easy --</option>
          <option value="MEDIUM">-- Medium --</option>
          <option value="HARD">-- Hard --</option>
        </Form.Select>
      </div>
      <div className="col-md-1">
        <Form.Label>Status</Form.Label>
        <Form.Select value={filter.active} onChange={(e) => setFilter({ ...filter, active: e.target.value })}>
          <option value=""> None </option>
          <option value="TRUE">-- Active --</option>
          <option value="FALSE">-- InActive --</option>
        </Form.Select>
      </div>
      <div className="col-md-2">
        <Form.Label>TimeLimit</Form.Label>
        <Form.Select value={filter.timeLimit} onChange={(e) => setFilter({ ...filter, timeLimit: e.target.value })}>
          <option value=""> None </option>
          <option value="30">-- 30 Phút --</option>
          <option value="45">-- 45 Phút --</option>
          <option value="60">-- 60 Phút --</option>
          <option value="90">-- 90 Phút --</option>
        </Form.Select>
      </div>

      <div className="col-md-1">
        <Form.Label>TotalParticipants</Form.Label>
        <Form.Control type="text" placeholder="Search by TotalParticipants" value={filter.totalParticipants} onChange={(e) => setFilter({ ...filter, totalParticipants: e.target.value })} />
      </div>

      <div className="d-flex justify-content-center gap-5">
        <div className="col-md-2 d-flex">
          <Button
            variant="primary"
            className="me-2"
            type="button" // ⚠️ Thêm dòng này để ngăn submit form
            onClick={() => handleSearch(1, filter)}
          >
            Search
          </Button>

          <Button variant="secondary" onClick={handleClear}>
            Clear
          </Button>
        </div>
      </div>
    </Form>
  );
};
export default FormQuizFilter;
