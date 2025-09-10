import { Pagination } from "react-bootstrap";

const AdvancedPagination = ({ metadata, onPageChange, filter }) => {
  const getPageNumbers = (current, total) => {
    let pages = []; // Tạo một mảng object rỗng, các loại định dang pagination

    if (total <= 5) {
      for (let i = 1; i <= total; i++) {
        // Nếu mà số lượng trang dưới 5 thì vẫn làm bình thường
        pages.push(i);
      }
    } else {
      // Ngược lại nếu số trang lớn hơn 5 sẽ bắt đầu phân định dạng
      if (current <= 3) {
        pages = [1, 2, 3, 4, "...", total]; // Nếu trang dưới 3 thì sẽ lấy 4 page đầu tiên, và page cuối
      } else if (current >= total - 2) {
        pages = [1, "...", total - 3, total - 2, total - 1, total]; // Nếu trỏ page ở gần cuối hoặc cuối
      } else {
        pages = [1, "...", current - 1, current, current + 1, "...", total]; // Nếu trỏ page ở giữa
      }
    }

    return pages;
  };
  const renderPaginationItems = () => {
    const pages = getPageNumbers(metadata.currentPage, metadata.totalPages); // Lấy định dạng page mới mỗi lần đổi trang

    return pages.map((page, index) => {
      if (page === "...") {
        // Nếu như phần tử trong mảng pages là "..." thì tạo ra 1 cái Ellipsis
        return <Pagination.Ellipsis key={`ellipsis-${index}`} disabled />;
      }
      return (
        // Tạo ra Item có thể bấm, active nếu đó là current
        <Pagination.Item key={page} active={page === metadata.currentPage} onClick={() => onPageChange(page, filter)}>
          {page}
        </Pagination.Item>
      );
    });
  };

  return (
    <div className="pagination">
      {metadata && metadata.totalPages > 0 && (
        <div className="d-flex justify-content-center mt-3">
          <Pagination>
            <Pagination.First disabled={metadata.currentPage === 1} onClick={() => onPageChange(1, filter)} /> {/* First */}
            <Pagination.Prev disabled={metadata.currentPage === 1} onClick={() => onPageChange(metadata.currentPage - 1, filter)} /> {/* Prev */}
            {renderPaginationItems()}
            <Pagination.Next disabled={metadata.currentPage === metadata.totalPages} onClick={() => onPageChange(metadata.currentPage + 1, filter)} /> {/* Next */}
            <Pagination.Last disabled={metadata.currentPage === metadata.totalPages} onClick={() => onPageChange(metadata.totalPages, filter)} />
            {/* Next */}
          </Pagination>
        </div>
      )}
    </div>
  );
};

export default AdvancedPagination;
