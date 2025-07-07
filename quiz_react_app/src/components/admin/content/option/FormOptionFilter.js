import { Button, Form } from "react-bootstrap";

const FormOptionFilter = ({ filter, setFilter, handleSearch, handleClear }) => {
  return (
    <Form className="row g-2 align-items-end">
      <div className="col-md-2">
        <Form.Label>ID</Form.Label>
        <Form.Control type="text" placeholder="Search by ID" value={filter.id} onChange={(e) => setFilter({ ...filter, id: e.target.value })} />
      </div>
      <div className="col-md-4">
        <Form.Label>Content</Form.Label>
        <Form.Control type="text" placeholder="Search by Content" value={filter.context} onChange={(e) => setFilter({ ...filter, context: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Correct Answer</Form.Label>
        <Form.Select value={filter.isCorrect} onChange={(e) => setFilter({ ...filter, isCorrect: e.target.value })}>
          <option value="">-- All --</option>
          <option value="true">True</option>
          <option value="false">False</option>
        </Form.Select>
      </div>
      <div className="col-md-2">
        <Form.Label>Question ID</Form.Label>
        <Form.Control type="text" placeholder="Search by Q.ID" value={filter.questionId} onChange={(e) => setFilter({ ...filter, questionId: e.target.value })} />
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

export default FormOptionFilter;
