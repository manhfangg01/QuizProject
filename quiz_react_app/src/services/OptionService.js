import axiosInstance from "../utils/axiosCustomize";

export const getAllOptions = async (pageNumber = 1, filter) => {
  return axiosInstance.get("/api/admin/options/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
};

export const getOptionById = async (optionId) => {
  return axiosInstance.get(`/api/admin/options/fetch/${optionId}`);
};

export const putUpdateOption = (optionId, context, isCorrect) => {
  return axiosInstance.put("/api/admin/options/update", {
    optionId,
    context,
    isCorrect,
  });
};

export const postCreateOption = async (context, isCorrect) => {
  return axiosInstance.post("/api/admin/options/create", {
    context,
    isCorrect,
  });
};

export const deleteOption = async (optionId) => {
  return axiosInstance.delete(`/api/admin/options/delete/${optionId}`);
};
