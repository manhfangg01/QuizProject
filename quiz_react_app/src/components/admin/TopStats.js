import { useEffect, useState } from "react";
import { getTopUsers } from "../../services/UserServices";
import { getTopQuizzes } from "../../services/QuizServices";

const TopStats = () => {
  const [users, setUsers] = useState([]);
  const [quizzes, setQuizzes] = useState([]);

  useEffect(() => {
    const fetchData = async () => {
      const resUsers = await getTopUsers();
      const resQuizzes = await getTopQuizzes();
      setUsers(resUsers.data || []);
      setQuizzes(resQuizzes.data || []);
    };
    fetchData();
  }, []);

  return (
    <div className="row mt-4">
      <div className="col-md-6">
        <h5>🏅 Top 5 người dùng điểm cao</h5>
        <ul className="list-group">
          {users.map((u, idx) => (
            <li key={idx} className="list-group-item d-flex justify-content-between">
              <span>{u.fullName}</span>
              <span>{u.averageScore.toFixed(2)}</span>
            </li>
          ))}
        </ul>
      </div>

      <div className="col-md-6">
        <h5>🔥 Top 5 Quiz phổ biến</h5>
        <ul className="list-group">
          {quizzes.map((q, idx) => (
            <li key={idx} className="list-group-item d-flex justify-content-between">
              <span>{q.title}</span>
              <span>{q.count}</span>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default TopStats;
