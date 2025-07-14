import { useEffect, useState } from "react";
import { getAdminStats } from "../../../services/UserServices";
import { toast } from "react-toastify";
import Button from "react-bootstrap/Button";
import { Link } from "react-router-dom";
import TopStats from "../TopStats";
import AccuracyPieChart from "../AccuracyPieChart";
import AvarageDuration from "../AvarageDuration";
const Dashboard = (props) => {
  const [stats, setStats] = useState({});
  const fetchAdminStats = async () => {
    try {
      const res = await getAdminStats();
      if (res.statusCode === 200) {
        setStats(res.data);
      } else {
        toast.warning("Không thể xác định quả trị viên");
      }
    } catch (err) {
      toast.warning(err);
    }
  };
  useEffect(() => {
    fetchAdminStats();
  }, []);

  return (
    <div className="admin-dashboard">
      <div className="dashboard-general">
        <h2>📊 Admin Dashboard</h2>
        <div className="row">
          <StatCard label="Tổng số Users" value={stats?.totalUsers} object="users" />
          <StatCard label="Tổng số Quizzes" value={stats?.totalQuizzes} object="quizzes" />
          <StatCard label="Tổng số Câu hỏi" value={stats?.totalQuestions} object="questions" />
          <StatCard label="Tổng số Lượt làm bài" value={stats?.totalResults} object="results" />
        </div>
      </div>

      <div className="dashboard-top-5">
        <TopStats />
      </div>
      <div className="dashboard-chart-time row" style={{ marginTop: "30px" }}>
        <div className="col-md-6 mb-4">
          <div className="dashboard-accuracy-chart shadow rounded p-3 bg-white h-100">
            <h5 className="text-center mb-3">🎯 Tỷ lệ câu trả lời đúng / sai</h5>
            <AccuracyPieChart />
          </div>
        </div>
        <div className="col-md-6 mb-4">
          <div className="dashboard-avarage-duration shadow rounded p-3 bg-white h-100 d-flex flex-column align-items-center justify-content-center">
            <AvarageDuration />
          </div>
        </div>
      </div>
    </div>
  );

  function StatCard({ label, value, object }) {
    return (
      <div className="col-md-3 mb-3 ">
        <div className="p-3 bg-light shadow rounded text-center admin-stats">
          <h5>{label}</h5>
          <h3>{value ?? "..."}</h3>
          <Link to={`/admins/manage-${object}`}>
            <Button>Xem chi tiết</Button>
          </Link>
        </div>
      </div>
    );
  }
};
export default Dashboard;
