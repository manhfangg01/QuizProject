import axiosCustom from "../utils/axiosCustomize";

export const getAccuracy = async () => {
  return await axiosCustom.get("/api/admin/answers/fetch-accuracy");
};
export const getDetailAnswer = async (answerId) => {
  return await axiosCustom.get(`/api/client/answers/${answerId}`);
};
