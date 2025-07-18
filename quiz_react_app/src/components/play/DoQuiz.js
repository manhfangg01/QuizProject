import { useEffect, useState } from "react";
import { Link, useParams, useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { displayQuiz, submitQuiz } from "../../services/QuizServices";
import "./DoQuiz.scss";
import { Button } from "react-bootstrap";

const DoQuiz = () => {
  const { id } = useParams();
  const [answers, setAnswers] = useState([]);
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [timeLeft, setTimeLeft] = useState(0);
  const [timer, setTimer] = useState(null);
  const [elapsedTime, setElapsedTime] = useState(0);

  const navigate = useNavigate();

  const fetchQuizData = async (quizId) => {
    try {
      setLoading(true);
      const response = await displayQuiz(quizId);
      if (response.statusCode === 200) {
        setQuiz(response.data);
      } else {
        toast.error("Failed to load quiz");
      }
    } catch (error) {
      toast.error(error.message || "An error occurred");
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins.toString().padStart(2, "0")}:${secs.toString().padStart(2, "0")}`;
  };

  const startTimer = () => {
    if (timer) clearInterval(timer);
    const newTimer = setInterval(() => {
      setTimeLeft((prev) => {
        if (prev <= 1) {
          clearInterval(newTimer);
          handleTimeUp();
          return 0;
        }
        const newTimeLeft = prev - 1;
        setElapsedTime(quiz.timeLimit * 60 - newTimeLeft);
        return newTimeLeft;
      });
    }, 1000);
    setTimer(newTimer);
  };

  const handleTimeUp = () => {
    toast.warning("Hết giờ! Tự động nộp bài...");
    handleSubmit();
  };

  const handleAnswer = (questionId, selectedOptionId) => {
    // Ghi đè nếu đã chọn trước đó
    setAnswers((prev) => {
      const updated = prev.filter((ans) => ans.questionId !== questionId);
      return [...updated, { questionId, selectedOptionId }];
    });
  };

  const scrollToQuestion = (questionId) => {
    const element = document.getElementById(`question-${questionId}`);
    element?.scrollIntoView({ behavior: "smooth", block: "start" });
  };

  const generateOptionLabelMap = () => {
    const labelList = ["A", "B", "C", "D"];
    const map = {};
    quiz.questions?.forEach((question) => {
      const optionMap = {};
      question.options?.forEach((option, idx) => {
        optionMap[option.optionId] = labelList[idx];
      });
      map[question.questionId] = optionMap;
    });
    return map;
  };

  const handleSubmit = async () => {
    try {
      const stored = localStorage.getItem("user");

      if (stored) {
        const userInfo = JSON.parse(stored);
        const optionLabelMap = generateOptionLabelMap();

        const res = await submitQuiz(id, userInfo.id, elapsedTime, answers, optionLabelMap);
        if (res.statusCode === 200) {
          if (timer) clearInterval(timer);
          toast.success("Quiz submitted successfully!");
          navigate(`/results/${res.data.resultId}`);
        } else {
          toast.warning("Gọi API thất bại");
        }
      }
    } catch (err) {
      toast.warning(err.message || "Lỗi khi nộp bài");
    }
  };

  useEffect(() => {
    fetchQuizData(id);

    const handleBeforeUnload = (event) => {
      event.preventDefault();
      event.returnValue = "Bạn có chắc muốn rời khỏi trang? Tiến trình quiz có thể bị mất!";
      return event.returnValue;
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      if (timer) clearInterval(timer);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [id]);

  useEffect(() => {
    if (quiz && quiz.timeLimit) {
      setTimeLeft(quiz.timeLimit * 60);
      startTimer();
    }
  }, [quiz]);

  if (loading) {
    return <div className="text-center py-5">Loading quiz...</div>;
  }

  if (!quiz) {
    return <div className="text-center py-5">No quiz data available</div>;
  }

  return (
    <div className="quiz-container">
      <div className="quiz-header d-flex justify-content-center align-items-center mb-5 gap-3">
        <h1>{quiz.title}</h1>
        <Link to={"/quizzes"}>
          <Button>Thoát</Button>
        </Link>
      </div>
      <div className="quiz-content d-flex">
        <div className="quiz-content flex-grow-1 pe-4">
          {quiz.questions?.map((question, index) => (
            <div key={question.questionId} id={`question-${question.questionId}`} className="question-card mb-4 p-3 border rounded">
              <h5 className="question-title fw-bold mb-3">
                {index + 1}. {question.context}
              </h5>
              <div className="options-list">
                {question.options?.map((option) => (
                  <div key={option.optionId} className="form-check mb-2">
                    <input
                      className="form-check-input"
                      type="radio"
                      name={`question-${question.questionId}`}
                      id={`option-${option.optionId}`}
                      onChange={() => handleAnswer(question.questionId, option.optionId)}
                    />
                    <label className="form-check-label" htmlFor={`option-${option.optionId}`}>
                      {option.content}
                    </label>
                  </div>
                ))}
              </div>
            </div>
          ))}
        </div>

        <div className="quiz-sidebar" style={{ width: "250px" }}>
          <div className="sidebar-card border rounded p-3 question-box">
            <div className="time-remaining mb-3">
              <h6 className="fw-bold">Thời gian còn lại:</h6>
              <div className="time-display fs-4">{formatTime(timeLeft)}</div>
            </div>

            <button className="submit-btn btn btn-primary w-100 mb-3" onClick={handleSubmit}>
              Nộp bài
            </button>

            <hr className="my-2" />

            <div className="question-navigation">
              <h6 className="fw-bold mb-2">Câu hỏi</h6>
              <div className="d-flex flex-wrap gap-2">
                {quiz.questions?.map((question, index) => (
                  <button
                    key={question.questionId}
                    className={`question-btn btn btn-sm ${answers.find((a) => a.questionId === question.questionId) ? "btn-success" : "btn-outline-secondary"}`}
                    onClick={() => scrollToQuestion(question.questionId)}
                  >
                    {index + 1}
                  </button>
                ))}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoQuiz;
