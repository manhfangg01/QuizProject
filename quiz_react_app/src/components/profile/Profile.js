import "./Profile.scss";
// import defaultAvatar from "../../assets/banner.webp";
import { useEffect, useState } from "react";
import { Tab, Nav } from "react-bootstrap";
import { FaPen } from "react-icons/fa";
import { Link } from "react-router-dom";
import { getProfile } from "../../services/UserServices";

const Profile = () => {
  const [user, setUser] = useState({});

  const getUserProfile = async (id) => {
    const res = await getProfile(id);
    if (res.statusCode === 200) {
      setUser({
        username: res.data.username,
        avatar: res.data.avatar,
        results: res.data.results,
      });
    }
  };
  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);

      getUserProfile(userInfo.id);
    }
  }, []);

  return (
    <div className="profile-container">
      <div className="banner" />

      <div className="avatar-wrapper">
        <img src={user.avatar} alt="avatar" className="avatar" />
        <Link to={"/my-account/setting"}>
          <span className="edit-icon">
            <FaPen />
          </span>
        </Link>
      </div>

      <div className="user-info text-center mt-3">
        <h4 className="username">{user.username}</h4>
        <span className="status-badge">Trang công khai</span>
      </div>

      <Tab.Container defaultActiveKey="courses">
        <div className="tab-section mt-4">
          <Nav variant="tabs" className="justify-content-center">
            <Nav.Item>
              <Nav.Link eventKey="courses">Khoá học</Nav.Link>
            </Nav.Item>
            <Nav.Item>
              <Nav.Link eventKey="results">Kết quả luyện thi</Nav.Link>
            </Nav.Item>
          </Nav>

          <Tab.Content className="tab-content p-3 text-center">
            <Tab.Pane eventKey="courses">
              <em>Bạn chưa đăng ký học khoá học nào!</em>{" "}
            </Tab.Pane>
            <Tab.Pane eventKey="results">{user.results?.length === 0 ? <em>Chưa có kết quả luyện thi nào.</em> : <div>Danh sách kết quả</div>}</Tab.Pane>
          </Tab.Content>
        </div>
      </Tab.Container>
    </div>
  );
};

export default Profile;
