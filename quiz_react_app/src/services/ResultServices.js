import axiosInstance from "../utils/axiosCustomize";

// export const getResultStatsPerDay = async () => {
//   const response = await axiosInstance.get("/api/admin/stat/result-per-day");
//   return response;
// };
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

export const getAvarageDuration = async () => {
  return await axiosInstance.get("/api/admin/results/fetch-avarage-time");
};

// Client
export const getHistory = async (pageNumber = 1, userId) => {
  const response = await axiosInstance.get(`/api/client/results/history/${userId}`, {
    params: {
      page: pageNumber,
    },
  });
  return response;
};

export const getDetailResult = async (resultId) => {
  return await axiosInstance.get(`/api/client/results/detail/${resultId}`);
};
