import { Button, Form } from "react-bootstrap";

const FormQuestionFilter = ({ filter, setFilter, handleSearch, handleClear }) => {
  return (
    <Form className="row g-2 align-items-end">
      <div className="col-md-2">
        <Form.Label>Question ID</Form.Label>
        <Form.Control type="text" placeholder="Search by ID" value={filter.id} onChange={(e) => setFilter({ ...filter, id: e.target.value })} />
      </div>
      <div className="col-md-4">
        <Form.Label>Question Content</Form.Label>
        <Form.Control type="text" placeholder="Search by content" value={filter.context} onChange={(e) => setFilter({ ...filter, context: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Quiz ID</Form.Label>
        <Form.Control type="text" placeholder="Search by Quiz ID" value={filter.quizId} onChange={(e) => setFilter({ ...filter, quizId: e.target.value })} />
      </div>
      <div className="col-md-2">
        <Form.Label>Options Count</Form.Label>
        <Form.Select value={filter.numberOfOptions} onChange={(e) => setFilter({ ...filter, numberOfOptions: e.target.value })}>
          <option value="">-- All --</option>
          <option value="2">2 options</option>
          <option value="3">3 options</option>
          <option value="4">4 options</option>
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

export default FormQuestionFilter;
