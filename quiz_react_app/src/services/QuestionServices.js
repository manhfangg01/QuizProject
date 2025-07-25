import axiosInstance from "../utils/SimpleAxiosCustomize";

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

export const putUpdateQuestion = async (questionId, context, optionIds, questionImage) => {
  const formData = new FormData();
  const question = {
    questionId,
    context,
    optionIds,
  };

  const questionBlob = new Blob([JSON.stringify(question)], {
    type: "application/json",
  });

  formData.append("updateQuestionRequest", questionBlob);

  if (questionImage) {
    formData.append("questionImage", questionImage);
  }

  const response = await axiosInstance.put("/api/admin/questions/update", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response;
};

export const postCreateQuestion = async (context, optionIds, questionImage) => {
  const formData = new FormData();
  const question = {
    context,
    optionIds,
  };
  const questionBlob = new Blob([JSON.stringify(question)], {
    type: "application/json",
  });
  formData.append("createQuestionRequest", questionBlob);
  if (questionImage) {
    formData.append("questionImage", questionImage);
  }
  const response = await axiosInstance.post("/api/admin/questions/create", formData);
  return response;
};
export const deleteQuestion = async (questionId) => {
  return axiosInstance.delete(`/api/admin/questions/delete/${questionId}`);
};
