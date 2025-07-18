import axios from "axios";
import axiosInstance from "../utils/axiosCustomize";
const instance = axios.create({
  baseURL: "http://localhost:8080",
});

export const getTopQuizzes = async () => {
  return await axiosInstance.get("/api/admin/quizzes/top-quizzes");
};
export const getAllQuizzes = async (pageNumber = 1, filter) => {
  const response = await axiosInstance.get("/api/admin/quizzes/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
  return response;
};

export const getQuizById = async (quizId) => {
  return await axiosInstance.get(`/api/admin/quizzes/fetch/${quizId}`);
};

export const postCreateQuiz = async (title, subjectName, timeLimit, difficulty, isActive, questions) => {
  return axiosInstance.post("/api/admin/quizzes/create", {
    title,
    subjectName,
    timeLimit,
    isActive,
    difficulty,
    questions,
  });
};
export const putUpdateQuiz = async (quizId, title, subjectName, timeLimit, difficulty, isActive, updatedQuestionIds) => {
  const response = await axiosInstance.put("/api/admin/quizzes/update", {
    quizId,
    title,
    subjectName,
    timeLimit,
    difficulty,
    isActive,
    questions: updatedQuestionIds,
  });

  return response;
};

export const deleteQuizById = async (quizId) => {
  return await axiosInstance.delete(`/api/admin/quizzes/delete/${quizId}`);
};

// Client
export const getLibraryQuizzes = async (pageNumber = 1, filter) => {
  return await instance.get("/api/client/quizzes/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
};

export const displayQuiz = async (id) => {
  return await axiosInstance.get(`/api/client/quizzes/display/${id}`);
};

export const submitQuiz = async (quizId, userId, duration, answers, optionLabelMap) => {
  return await axiosInstance.post("/api/client/quizzes/submit", {
    quizId,
    userId,
    duration,
    answers,
    optionLabelMap,
  });
};
