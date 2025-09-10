import { useState } from "react";
import { Modal, Table, Form, Button } from "react-bootstrap";
import FormOptionFilter from "../../option/FormOptionFilter";
import AdvancedPagination from "../../AdvancedPagination";

const SelectOptionModal = ({ show, setShow, availableOptions = [], setSelectedOptionIds, metadata, fetchOptions }) => {
  const [selectedIds, setSelectedIds] = useState([]);
  const [filter, setFilter] = useState({
    id: "",
    context: "",
    isCorrect: "",
    questionId: "",
  });
  const handleClose = () => {
    setShow(false);
    setSelectedIds([]);
    setFilter({});
  };

  const handleSearch = () => {
    fetchOptions(1, filter);
  };

  const handleClear = () => {
    const reset = { id: "", context: "", isCorrect: "", questionId: "" };
    setFilter(reset);
    fetchOptions(1, reset);
  };

  const handleSelect = (optionId) => {
    setSelectedIds((prev) => (prev.includes(optionId) ? prev.filter((id) => id !== optionId) : [...prev, optionId]));
  };

  const handleSubmit = async () => {
    setSelectedOptionIds(selectedIds);
    setSelectedIds([]);
    setShow(false);
    fetchOptions(1, {});
  };
  const onPageChange = (pageNumber, filter) => {
    if (pageNumber === metadata.currentPage) return;
    fetchOptions(pageNumber, filter);
  };

  return (
    <div>
      <Modal show={show} onHide={() => setShow(false)} size="xl">
        <Modal.Header closeButton>
          <Modal.Title>Select Options</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div className="filter-manager mb-3">
            <FormOptionFilter filter={filter} setFilter={setFilter} handleClear={handleClear} handleSearch={handleSearch} />
          </div>
          <Table striped bordered hover style={{ minHeight: "300px" }}>
            <thead>
              <tr>
                <th>Select</th>
                <th>Content</th>
                <th>Is Correct</th>
              </tr>
            </thead>
            <tbody>
              {Array.isArray(availableOptions) && availableOptions.length > 0 ? (
                availableOptions.map((option) => (
                  <tr key={option.id}>
                    <td>
                      <Form.Check type="checkbox" checked={selectedIds.includes(option.id)} onChange={() => handleSelect(option.id)} />
                    </td>
                    <td>{option.context}</td>
                    <td>{option.isCorrect ? "✓" : "✗"}</td>
                  </tr>
                ))
              ) : (
                <tr>
                  <td colSpan={3} className="text-center">
                    Không có lựa chọn nào.
                  </td>
                </tr>
              )}
            </tbody>
          </Table>
        </Modal.Body>
        <Modal.Footer>
          <div className="col-12">
            <AdvancedPagination metadata={metadata} onPageChange={onPageChange} filter={filter} />
          </div>
          <Button variant="secondary" onClick={handleClose}>
            Cancel
          </Button>
          <Button variant="primary" onClick={handleSubmit}>
            Confirm Selection
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
};

export default SelectOptionModal;
