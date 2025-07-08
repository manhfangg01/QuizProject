import axiosInstance from "../utils/axiosCustomize";

export const getAllQuestions = async (pageNumber = 1, filter) => {
  return axiosInstance.get("/api/admin/questions/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
};

export const getQuestionById = async (questionId) => {
  return axiosInstance.get(`/api/admin/questions/fetch/${questionId}`);
};

export const putUpdateQuestion = (questionId, context, optionIds) => {
  return axiosInstance.put("/api/admin/questions/update", {
    questionId,
    context,
    optionIds,
  });
};

export const postCreateQuestion = async (context, optionIds) => {
  return axiosInstance.post("/api/admin/questions/create", {
    context,
    optionIds,
  });
};

export const deleteQuestion = async (questionId) => {
  return axiosInstance.delete(`/api/admin/questions/delete/${questionId}`);
};
