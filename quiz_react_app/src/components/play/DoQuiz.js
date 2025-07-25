import { useEffect, useState } from "react";
import { Link, useParams, useNavigate, useSearchParams } from "react-router-dom";
import { toast } from "react-toastify";
import { displayQuiz, submitQuiz } from "../../services/QuizServices";
import "./DoQuiz.scss";
import { Button, Spinner } from "react-bootstrap";

const DoQuiz = () => {
  const { id } = useParams();
  const [searchParams] = useSearchParams();
  const timeParam = searchParams.get("time");

  const [answers, setAnswers] = useState([]);
  const [quiz, setQuiz] = useState(null);
  const [loading, setLoading] = useState(true);
  const [timeLeft, setTimeLeft] = useState(0);
  const [timer, setTimer] = useState(null);
  const [elapsedTime, setElapsedTime] = useState(0);
  const [isUnlimitedTime, setIsUnlimitedTime] = useState(false);

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
      setElapsedTime((prev) => prev + 1);

      if (!isUnlimitedTime) {
        setTimeLeft((prev) => {
          if (prev <= 1) {
            clearInterval(newTimer);
            handleTimeUp();
            return 0;
          }
          return prev - 1;
        });
      }
    }, 1000);

    setTimer(newTimer);
  };

  const handleTimeUp = () => {
    toast.warning("Time's up! Submitting your quiz...");
    handleSubmit();
  };

  const handleAnswer = (questionId, selectedOptionId) => {
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
      if (isUnlimitedTime && elapsedTime < 60) {
        toast.warning("Please spend at least 1 minute on the quiz");
        return;
      }

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
          toast.warning("Failed to submit quiz");
        }
      } else {
        toast.warning("Please login to submit the quiz");
      }
    } catch (err) {
      toast.error(err.message || "Error submitting quiz");
    }
  };

  useEffect(() => {
    fetchQuizData(id);
    setIsUnlimitedTime(timeParam === "infinity");

    const handleBeforeUnload = (event) => {
      if (answers.length > 0) {
        event.preventDefault();
        event.returnValue = "You have unsaved answers. Are you sure you want to leave?";
      }
      return event.returnValue;
    };

    window.addEventListener("beforeunload", handleBeforeUnload);

    return () => {
      if (timer) clearInterval(timer);
      window.removeEventListener("beforeunload", handleBeforeUnload);
    };
  }, [id, timeParam]);

  useEffect(() => {
    if (quiz) {
      if (isUnlimitedTime) {
        setTimeLeft(0);
        setElapsedTime(0);
      } else {
        const timeLimit = timeParam ? parseInt(timeParam) : quiz.timeLimit;
        setTimeLeft(timeLimit * 60);
        setElapsedTime(0);
      }
      startTimer();
    }
  }, [quiz, isUnlimitedTime, timeParam]);

  if (loading) {
    return (
      <div className="text-center py-5">
        <Spinner animation="border" variant="primary" />
        <p>Loading quiz...</p>
      </div>
    );
  }

  if (!quiz) {
    return <div className="text-center py-5">No quiz data available</div>;
  }

  return (
    <div className="quiz-container">
      <div className="quiz-header d-flex justify-content-between align-items-center mb-4 p-3 bg-light rounded">
        <h2 className="mb-0">{quiz.title}</h2>
        <div className="d-flex gap-2">
          <Button variant="outline-danger" onClick={() => window.confirm("Are you sure you want to exit?") && navigate("/quizzes")}>
            Exit Quiz
          </Button>
        </div>
      </div>

      <div className="quiz-content d-flex">
        <div className="quiz-questions flex-grow-1 pe-4">
          {quiz.questions?.map((question, index) => (
            <div key={question.questionId} id={`question-${question.questionId}`} className="question-card mb-4 p-4 border rounded shadow-sm">
              <div className="d-flex align-items-start">
                {/* Question Image on the left */}
                {question.questionImage && (
                  <div className="me-4" style={{ width: "500px", flexShrink: 0 }}>
                    <img
                      src={question.questionImage}
                      alt={`Illustration for question ${index + 1}`}
                      className="img-fluid rounded"
                      style={{
                        maxHeight: "500px",
                        width: "100%",
                        objectFit: "cover",
                        border: "1px solid #dee2e6",
                      }}
                    />
                  </div>
                )}

                {/* Question content on the right */}
                <div style={{ flex: 1 }}>
                  <h4 className="question-title fw-bold mb-3">
                    <span className="text-primary">Question {index + 1}:</span> {question.context}
                  </h4>

                  <div className="options-list">
                    {question.options?.map((option) => (
                      <div key={option.optionId} className="form-check mb-3">
                        <input
                          className="form-check-input"
                          type="radio"
                          name={`question-${question.questionId}`}
                          id={`option-${option.optionId}`}
                          onChange={() => handleAnswer(question.questionId, option.optionId)}
                          checked={answers.some((a) => a.questionId === question.questionId && a.selectedOptionId === option.optionId)}
                        />
                        <label className="form-check-label fs-5" htmlFor={`option-${option.optionId}`}>
                          {option.content}
                        </label>
                      </div>
                    ))}
                  </div>
                </div>
              </div>
            </div>
          ))}
        </div>

        <div className="quiz-sidebar" style={{ width: "300px" }}>
          <div className="sidebar-card border rounded p-3 shadow-sm sticky-top" style={{ top: "20px" }}>
            <div className="time-display mb-4 text-center">
              <h5 className="fw-bold">{isUnlimitedTime ? "Time Elapsed" : "Time Remaining"}</h5>
              <div className={`display-4 ${!isUnlimitedTime && timeLeft < 300 ? "text-danger" : "text-primary"}`}>{isUnlimitedTime ? formatTime(elapsedTime) : formatTime(timeLeft)}</div>
              {isUnlimitedTime ? <small className="text-muted">No time limit</small> : <small className="text-muted">{timeParam || quiz.timeLimit} minute quiz</small>}
            </div>

            <Button variant="primary" size="lg" className="w-100 mb-3 fw-bold" onClick={handleSubmit}>
              Submit Quiz
            </Button>

            <hr className="my-3" />

            <div className="question-progress">
              <h5 className="fw-bold mb-3 text-center">Question Navigation</h5>
              <div className="d-flex flex-wrap gap-2 justify-content-center">
                {quiz.questions?.map((question, index) => (
                  <Button
                    key={question.questionId}
                    variant={answers.some((a) => a.questionId === question.questionId) ? "success" : "outline-secondary"}
                    size="sm"
                    className="fs-5"
                    style={{ width: "40px", height: "40px" }}
                    onClick={() => scrollToQuestion(question.questionId)}
                  >
                    {index + 1}
                  </Button>
                ))}
              </div>
              <div className="mt-3 text-center">
                <small className="text-muted">
                  Completed: {answers.length}/{quiz.questions?.length}
                </small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default DoQuiz;
