import axiosInstance from "../utils/SimpleAxiosCustomize";

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

export const putUpdateQuiz = async (quizId, title, subjectName, timeLimit, difficulty, isActive, updatedQuestionIds, audioFile) => {
  const formData = new FormData();

  // Tạo object chứa thông tin quiz (không gồm file)
  const quizData = {
    quizId,
    title,
    subjectName,
    timeLimit,
    difficulty,
    isActive,
    questions: updatedQuestionIds,
  };

  const quizBlob = new Blob([JSON.stringify(quizData)], {
    type: "application/json",
  });

  formData.append("updateQuizRequest", quizBlob);

  if (audioFile) {
    formData.append("audioFile", audioFile);
  }

  const response = await axiosInstance.put("/api/admin/quizzes/update", formData);
  return response;
};

export const postCreateQuiz = async (title, subjectName, timeLimit, difficulty, isActive, questions, audioFile) => {
  const formData = new FormData();

  // Tạo object chứa thông tin quiz (không gồm file)
  const quizData = {
    title,
    subjectName,
    timeLimit,
    difficulty,
    isActive,
    questions, // Mảng các câu hỏi (object), backend cần hỗ trợ parse mảng JSON
  };

  const quizBlob = new Blob([JSON.stringify(quizData)], {
    type: "application/json",
  });

  formData.append("createQuizRequest", quizBlob); // giống như @RequestPart("createQuizRequest")

  if (audioFile) {
    formData.append("audioFile", audioFile); // giống như @RequestParam MultipartFile audioFile
  }

  const response = await axiosInstance.post("/api/admin/quizzes/create", formData);
  return response;
};

export const deleteQuestion = async (questionId) => {
  return axiosInstance.delete(`/api/admin/questions/delete/${questionId}`);
};

export const deleteQuizById = async (quizId) => {
  return await axiosInstance.delete(`/api/admin/quizzes/delete/${quizId}`);
};

// Client
export const getLibraryQuizzes = async (pageNumber = 1, filter) => {
  return await axiosInstance.get("/api/client/quizzes/fetch", {
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

export const showDetailQuiz = async (id) => {
  return await axiosInstance.get(`/api/client/quizzes/fetch/${id}`);
};
