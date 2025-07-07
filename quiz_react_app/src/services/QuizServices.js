import axiosInstance from "../utils/axiosCustomize";
export const getAllQuizzesService = async (pageNumber = 1, filter) => {
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

export const postCreateNewQuiz = async (email, password, fullName, role, imageFile) => {
  const formData = new FormData();
  const quiz = {
    email,
    password,
    fullName,
    role,
  };
  const quizBlob = new Blob([JSON.stringify(quiz)], {
    type: "application/json",
  });
  formData.append("createQuizRequest", quizBlob);
  if (imageFile) {
    formData.append("quizAvatar", imageFile);
  }
  const response = await axiosInstance.post("/api/admin/quizzes/create", formData); // không cần gửi đi bearer token thủ công nữa custom đã tự gắn rồi
  return response;
};

export const putUpdateQuiz = async (quizId, fullName, role, imageFile) => {
  const formData = new FormData();

  const quiz = {
    quizId,
    fullName,
    role,
  };

  console.log(">>Debug", quiz);

  const quizBlob = new Blob([JSON.stringify(quiz)], {
    type: "application/json",
  });

  formData.append("updateQuizRequest", quizBlob);

  if (imageFile) {
    formData.append("quizAvatar", imageFile);
  }
  const response = await axiosInstance.post("/api/admin/quizzes/update", formData);

  return response;
};

export const deleteQuizById = async (quizId) => {
  return await axiosInstance.delete(`/api/admin/quizzes/delete/${quizId}`);
};
