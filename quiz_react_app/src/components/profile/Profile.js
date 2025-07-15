import "./Profile.scss";
// import defaultAvatar from "../../assets/banner.webp";
import { useEffect, useState } from "react";
import { Tab, Nav } from "react-bootstrap";
import { FaPen } from "react-icons/fa";
import { Link } from "react-router-dom";
import { getProfile } from "../../services/UserServices";
import { getHistory } from "../../services/ResultServices";
import { toast } from "react-toastify";
import CustomPagination from "../admin/content/CustomPagination";

const Profile = () => {
  const [user, setUser] = useState({});
  const [history, setHistory] = useState([]);
  const [metadata, setMetadata] = useState({});

  const getUserProfile = async (id) => {
    const res = await getProfile(id);
    if (res.statusCode === 200) {
      setUser({
        username: res.data.username,
        avatar: res.data.avatar,
      });
    }
  };

  const fetchHistoryUser = async (pageNumber) => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      try {
        const res = await getHistory(pageNumber, userInfo.id);
        if (res.statusCode === 200) {
          const { histories, metadata } = res.data;
          setHistory(histories); // cập nhật state
          setMetadata(metadata);
        } else {
          toast.warning("Gọi API thất bại");
        }
      } catch (err) {
        toast.warning("Lỗi khi gọi API lịch sử");
      }
    }
  };

  useEffect(() => {
    const stored = localStorage.getItem("user");
    if (stored) {
      const userInfo = JSON.parse(stored);
      getUserProfile(userInfo.id);
    }
    fetchHistoryUser(1);
  }, []);

  const onPageChange = (pageNumber) => {
    if (pageNumber === metadata.currentPage) return;
    fetchHistoryUser(pageNumber);
  };

  const formatDuration = (seconds) => {
    const hrs = String(Math.floor(seconds / 3600)).padStart(2, "0");
    const mins = String(Math.floor((seconds % 3600) / 60)).padStart(2, "0");
    const secs = String(seconds % 60).padStart(2, "0");
    return `${hrs}:${mins}:${secs}`;
  };

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
              <Nav.Link eventKey="history">Kết quả luyện thi</Nav.Link>
            </Nav.Item>
          </Nav>

          <Tab.Content className="tab-content p-3 text-center">
            <Tab.Pane eventKey="history">
              {history.length === 0 ? (
                <em>Chưa có kết quả luyện thi nào.</em>
              ) : (
                <>
                  <div className="text-end mb-3 d-flex justify-content-center">
                    <Link to="/results/statistics" className="btn btn-outline-primary">
                      📊 Tới trang thống kê kết quả luyện thi
                    </Link>
                  </div>
                  {history.map((item, index) => (
                    <div key={index} className="result-card mb-4 p-3 border rounded">
                      <h5>{item.quizTitle}</h5>
                      <div className="d-flex justify-content-between mt-2">
                        <div>
                          <strong>Ngày làm:</strong> {new Date(item.submittedAt).toISOString().slice(0, 10)}
                        </div>

                        <div>
                          <strong>Kết quả:</strong> {item.totalCorrect}/{item.totalQuestions} (Điểm: {item.score})
                        </div>
                        <div>
                          <strong>Thời gian làm bài:</strong> {formatDuration(item.duration)}
                        </div>
                        <div>
                          <Link to={`/results/${item.resultId}`}>Xem chi tiết</Link>
                        </div>
                      </div>
                    </div>
                  ))}

                  <CustomPagination metadata={metadata} onPageChange={onPageChange} />
                </>
              )}
            </Tab.Pane>
          </Tab.Content>
        </div>
      </Tab.Container>
    </div>
  );
};

export default Profile;
