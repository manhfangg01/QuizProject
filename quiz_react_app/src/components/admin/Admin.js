import SideBar from "./SideBar";
import { FaBars } from "react-icons/fa";
import "./Admin.scss";
import { useEffect, useState } from "react";
import { Outlet, useNavigate } from "react-router-dom";
import { Bounce, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { NavDropdown } from "react-bootstrap";
import defaultAvatar from "../../assets/default.jpg";
import { callLogout } from "../../services/AuthServices";
const Admin = (props) => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem("accessToken"); // phải đúng key bạn đang dùng
      const userData = localStorage.getItem("user");

      if (token && userData) {
        try {
          setUserInfo(JSON.parse(userData));
          setIsAuthenticated(true);
        } catch (e) {
          console.error("User parse error:", e);
        }
      }
    };
    checkAuth();
    // Lắng nghe sự kiện login thành công
    window.addEventListener("loginSuccess", checkAuth);
    // cleanup
    return () => window.removeEventListener("loginSuccess", checkAuth);
  }, []);

  const handleLogout = () => {
    callLogout();
    localStorage.removeItem("access_token");
    localStorage.removeItem("user");
    setIsAuthenticated(false);
    setUserInfo(null);
    navigate("/");
  };
  return (
    <div className="admin-container">
      <div className="admin-sidebar">
        <SideBar collapsed={collapsed} />
      </div>
      <div className="admin-content">
        <div className="admin-header">
          <FaBars
            onClick={() => {
              setCollapsed(!collapsed);
            }}
          />
          {isAuthenticated && (
            <NavDropdown
              title={<img src={userInfo.avatar === "" ? defaultAvatar : userInfo.avatar} alt="avatar" className="rounded-circle" style={{ width: "40px", height: "40px", objectFit: "cover" }} />}
              id="user-nav-dropdown"
              align="end"
            >
              <NavDropdown.Item onClick={handleLogout}>Đăng xuất</NavDropdown.Item>
            </NavDropdown>
          )}
        </div>
        <div className="admin-main">
          <Outlet />
        </div>
      </div>

      <ToastContainer
        position="top-center"
        autoClose={5000}
        hideProgressBar={false}
        newestOnTop={false}
        closeOnClick={false}
        rtl={false}
        pauseOnFocusLoss
        draggable
        pauseOnHover
        theme="light"
        transition={Bounce}
      />
    </div>
  );
};
export default Admin;
