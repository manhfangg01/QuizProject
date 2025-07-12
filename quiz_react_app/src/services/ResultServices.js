import axiosInstance from "../utils/axiosCustomize";

export const getAllResults = async (pageNumber = 1, filter) => {
  const response = await axiosInstance.get("/api/admin/results/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
  return response;
};

export const getAllResultByUserId = async (pageNumber = 1, filter, userId) => {
  const response = await axiosInstance.get(`/api/admin/results/user/fetch/${userId}`, {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
  return response;
};

export const getAllResultByQuizId = async (pageNumber = 1, filter, quizId) => {
  const response = await axiosInstance.get(`/api/admin/results/quiz/fetch/${quizId}`, {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
  return response;
};

export const getResultById = async (id) => {
  const response = await axiosInstance.get(`/api/admin/results/fetch/${id}`);
  return response;
};

export const deleteResultById = async (id) => {
  const response = await axiosInstance.delete(`/api/admin/results/delete/${id}`);
  return response;
};
