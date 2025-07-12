import { Button, Form } from "react-bootstrap";
import { useState } from "react";

const FormResultFilter = ({ filter, setFilter, fetchAllResults }) => {
  const [dateRange, setDateRange] = useState({
    from: "",
    to: "",
  });

  const handleSearch = () => {
    const formattedFilter = {
      ...filter,
      from: dateRange.from ? new Date(dateRange.from).toISOString() : null,
      to: dateRange.to ? new Date(dateRange.to).toISOString() : null,
    };
    fetchAllResults(1, formattedFilter);
  };

  const handleClear = () => {
    const reset = {
      id: "",
      userId: "",
      quizId: "",
      score: "",
      totalCorrects: "",
      duration: "",
    };
    setFilter(reset);
    setDateRange({ from: "", to: "" });
    fetchAllResults(1, reset);
  };

  return (
    <Form className="row g-2 align-items-end d-flex justify-content-center">
      <div className="col-md-2">
        <Form.Label>Result ID</Form.Label>
        <Form.Control type="text" placeholder="Search by Result ID" value={filter.id} onChange={(e) => setFilter({ ...filter, id: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>User ID</Form.Label>
        <Form.Control type="text" placeholder="Search by User ID" value={filter.userId} onChange={(e) => setFilter({ ...filter, userId: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Quiz ID</Form.Label>
        <Form.Control type="text" placeholder="Search by Quiz ID" value={filter.quizId} onChange={(e) => setFilter({ ...filter, quizId: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Date From</Form.Label>
        <Form.Control type="date" value={dateRange.from} onChange={(e) => setDateRange({ ...dateRange, from: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Date To</Form.Label>
        <Form.Control type="date" value={dateRange.to} onChange={(e) => setDateRange({ ...dateRange, to: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Min Score</Form.Label>
        <Form.Control type="number" placeholder="Min score" value={filter.score} onChange={(e) => setFilter({ ...filter, score: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Correct Answers</Form.Label>
        <Form.Control type="number" placeholder="Correct answers" value={filter.totalCorrects} onChange={(e) => setFilter({ ...filter, totalCorrects: e.target.value })} />
      </div>

      <div className="col-md-2">
        <Form.Label>Duration (seconds)</Form.Label>
        <Form.Control type="number" placeholder="Duration" value={filter.duration} onChange={(e) => setFilter({ ...filter, duration: e.target.value })} />
      </div>

      <div className="d-flex justify-content-center gap-5 mt-3">
        <div className="col-md-3 d-flex">
          <Button variant="primary" className="me-2" onClick={handleSearch}>
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

export default FormResultFilter;
