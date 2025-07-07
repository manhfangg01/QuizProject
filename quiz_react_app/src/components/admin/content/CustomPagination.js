import { Pagination } from "react-bootstrap";

const CustomPagination = ({ metadata, onPageChange }) => {
  return (
    <div className="pagination">
      {metadata && metadata.totalPages > 0 && (
        <div className="d-flex justify-content-center mt-3">
          <Pagination>
            <Pagination.First disabled={metadata.currentPage === 1} onClick={() => onPageChange(1)} />
            <Pagination.Prev disabled={metadata.currentPage === 1} onClick={() => onPageChange(metadata.currentPage - 1)} />

            {[...Array(metadata.totalPages)].map((_, index) => {
              const page = index + 1;
              return (
                <Pagination.Item key={page} active={page === metadata.currentPage} onClick={() => onPageChange(page)}>
                  {page}
                </Pagination.Item>
              );
            })}

            <Pagination.Next disabled={metadata.currentPage === metadata.totalPages} onClick={() => onPageChange(metadata.currentPage + 1)} />
            <Pagination.Last disabled={metadata.currentPage === metadata.totalPages} onClick={() => onPageChange(metadata.totalPages)} />
          </Pagination>
        </div>
      )}
    </div>
  );
};
export default CustomPagination;
