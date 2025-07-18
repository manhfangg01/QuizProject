import { useNavigate } from "react-router-dom";
import "./Unauthenticated.scss";

const Unauthenticated = () => {
  const navigate = useNavigate();

  return (
    <div className="unauthenticated-container">
      <div className="unauthenticated-content">
        <div className="error-code">401</div>
        <h1 className="error-title">Chưa xác thực</h1>
        <p className="error-message">Bạn cần đăng nhập để truy cập trang này.</p>
        <div className="error-details">
          <p>Nếu bạn đã có tài khoản, vui lòng đăng nhập để tiếp tục.</p>
          <p>Nếu chưa có tài khoản, bạn có thể đăng ký mới.</p>
        </div>
        <div className="action-buttons">
          <button onClick={() => navigate("/login")} className="btn-login">
            Đăng nhập
          </button>
          <button onClick={() => navigate("/register")} className="btn-register">
            Đăng ký
          </button>
          <button onClick={() => navigate("/")} className="btn-go-home">
            Về trang chủ
          </button>
        </div>
        <div className="support-contact">
          <p>
            Cần hỗ trợ? Gửi email tới: <a href="mailto:support@study4.com">support@study4.com</a>
          </p>
        </div>
      </div>
    </div>
  );
};

export default Unauthenticated;
