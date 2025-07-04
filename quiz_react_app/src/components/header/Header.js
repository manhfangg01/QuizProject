import Container from "react-bootstrap/Container";
import Nav from "react-bootstrap/Nav";
import Navbar from "react-bootstrap/Navbar";
import NavDropdown from "react-bootstrap/NavDropdown";
import { NavLink, Link, useNavigate } from "react-router-dom";
import { useEffect, useState } from "react";
import defaultAvatar from "../../assets/default.jpg"; // ảnh mặc định nếu user chưa có avatar
import { callLogout } from "../../services/AuthServices";

const Header = () => {
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
    <Navbar expand="lg" className="bg-body-tertiary">
      <Container>
        <NavLink className="navbar-brand" to="/">
          QuizProject
        </NavLink>
        <Navbar.Toggle aria-controls="basic-navbar-nav" />
        <Navbar.Collapse id="basic-navbar-nav">
          <Nav className="me-auto">
            <NavLink to="/" className="nav-link">
              Home
            </NavLink>
            <NavLink to="/admins" className="nav-link">
              Admin
            </NavLink>
            <NavLink to="/users" className="nav-link">
              User
            </NavLink>
          </Nav>

          <Nav>
            {!isAuthenticated ? (
              <>
                <Link className="btn-login btn" to="/login">
                  Log in
                </Link>
                <Link className="btn-signup btn" to="/signup">
                  Sign up
                </Link>
              </>
            ) : (
              <NavDropdown
                title={<img src={userInfo.avatar === "" ? defaultAvatar : userInfo.avatar} alt="avatar" className="rounded-circle" style={{ width: "40px", height: "40px", objectFit: "cover" }} />}
                id="user-nav-dropdown"
                align="end"
              >
                <NavDropdown.Item onClick={() => navigate("/profile")}>Trang cá nhân</NavDropdown.Item>
                <NavDropdown.Divider />
                <NavDropdown.Item onClick={handleLogout}>Đăng xuất</NavDropdown.Item>
              </NavDropdown>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
};

export default Header;
