import SideBar from "./SideBar";
import { FaBars, FaHome } from "react-icons/fa";
import "./Admin.scss";
import { useEffect, useState } from "react";
import { Link, Outlet, useNavigate } from "react-router-dom";
import { Bounce, ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";
import { NavDropdown, Badge } from "react-bootstrap";
import defaultAvatar from "../../assets/default.jpg";
import { callLogout } from "../../services/AuthServices";
import { GrUserAdmin } from "react-icons/gr";
import DarkModeToggle from "./DarkModeToggle";

const Admin = (props) => {
  const [collapsed, setCollapsed] = useState(false);
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState(null);

  useEffect(() => {
    const checkAuth = () => {
      const token = localStorage.getItem("accessToken");
      const userData = localStorage.getItem("user");

      if (token && userData) {
        try {
          const parsedUser = JSON.parse(userData);
          setUserInfo(parsedUser);
          setIsAuthenticated(true);

          // 👉 Kiểm tra role tại đây:
          if (parsedUser.role !== "ADMIN") {
            navigate("/unauthorized");
          }
        } catch (e) {
          console.error("User parse error:", e);
        }
      } else {
        navigate("/unauthorized");
      }
    };

    checkAuth();
    window.addEventListener("loginSuccess", checkAuth);
    return () => window.removeEventListener("loginSuccess", checkAuth);
  }, [navigate]);
  const handleLogout = () => {
    callLogout();
    localStorage.removeItem("accessToken");
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
          <div className="menu-toggle">
            <FaBars onClick={() => setCollapsed(!collapsed)} />
          </div>
          <div className="home-click">
            <a href="/" className="btn" style={{ fontSize: "30px" }}>
              <FaHome />
            </a>
          </div>

          <div className="d-flex align-items-center gap-4">
            <div className="position-relative">
              <DarkModeToggle />
            </div>

            {isAuthenticated && (
              <NavDropdown
                title={
                  <div className="d-flex align-items-center ">
                    <img
                      src={userInfo?.avatar || defaultAvatar}
                      alt="avatar"
                      className="rounded-circle"
                      style={{
                        width: "40px",
                        height: "40px",
                        objectFit: "cover",
                        border: "2px solid #eee",
                      }}
                    />
                  </div>
                }
                id="user-nav-dropdown"
                align="end"
              >
                <NavDropdown.Item onClick={(e) => e.stopPropagation()}>
                  Xin chào <GrUserAdmin />
                  <span className="user-name ms-2 d-none d-md-inline" style={{ color: "#35509a" }}>
                    {userInfo?.fullName || "Admin"}
                  </span>
                </NavDropdown.Item>
                <NavDropdown.Item onClick={() => navigate("/admins/manage-profile")}>Hồ sơ</NavDropdown.Item>
                {/* <NavDropdown.Item >Cài đặt</NavDropdown.Item> */}
                <NavDropdown.Divider />
                <NavDropdown.Item
                  onClick={() => {
                    handleLogout();
                  }}
                >
                  Đăng xuất
                </NavDropdown.Item>
              </NavDropdown>
            )}
          </div>
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
