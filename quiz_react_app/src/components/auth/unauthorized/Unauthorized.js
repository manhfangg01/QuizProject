import { useNavigate } from "react-router-dom";
import "./Unauthorized.scss";

const Unauthorized = () => {
  const navigate = useNavigate();

  return (
    <div className="unauthorized-container">
      <div className="unauthorized-content">
        <div className="error-code">403</div>
        <h1 className="error-title">Truy cập bị từ chối</h1>
        <p className="error-message">Xin lỗi, bạn không có quyền truy cập trang này.</p>
        <div className="error-details">
          <p>Nếu bạn cho rằng đây là lỗi, vui lòng liên hệ với quản trị viên.</p>
          <p>Hoặc bạn có thể:</p>
        </div>
        <div className="action-buttons">
          <button onClick={() => navigate(-1)} className="btn-go-back">
            Quay lại trang trước
          </button>
          <button onClick={() => navigate("/")} className="btn-go-home">
            Về trang chủ
          </button>
        </div>
        <div className="support-contact">
          <p>
            Liên hệ hỗ trợ: <a href="mailto:support@study4.com">support@study4.com</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Unauthorized;
