import axiosInstance from "../utils/SimpleAxiosCustomize";

export const getTopUsers = async () => {
  return await axiosInstance.get("/api/admin/stat/top-users");
};

export const getAdminStats = async () => {
  return await axiosInstance.get("/api/dashboard");
};

export const getAllUsersService = async (pageNumber = 1, filter) => {
  const response = await axiosInstance.get("/api/admin/users/fetch", {
    params: {
      page: pageNumber,
      ...filter,
    },
  });
  return response;
};

export const postCreateNewUser = async (email, password, fullName, role, imageFile) => {
  const formData = new FormData();
  const user = {
    email,
    password,
    fullName,
    role,
  };
  const userBlob = new Blob([JSON.stringify(user)], {
    type: "application/json",
  });
  formData.append("createUserRequest", userBlob);
  if (imageFile) {
    formData.append("userAvatar", imageFile);
  }
  const response = await axiosInstance.post("/api/admin/users/create", formData); // không cần gửi đi bearer token thủ công nữa custom đã tự gắn rồi
  return response;
};

export const putUpdateUser = async (userId, fullName, role, imageFile) => {
  const formData = new FormData();

  const user = {
    userId,
    fullName,
    role,
  };

  const userBlob = new Blob([JSON.stringify(user)], {
    type: "application/json",
  });

  formData.append("updateUserRequest", userBlob);

  if (imageFile) {
    formData.append("userAvatar", imageFile);
  }
  const response = await axiosInstance.post("/api/admin/users/update", formData);

  return response;
};

export const deleteUserById = async (userId) => {
  return await axiosInstance.delete(`/api/admin/users/delete/${userId}`);
};
export const getUserById = async (userId) => {
  return await axiosInstance.get(`/api/admin/users/fetch/${userId}`);
};

// client
export const getProfile = async (id) => {
  return await axiosInstance.get(`/api/client/users/profile/${id}`);
};

export const putUpdateProfile = async (id, fullName, about, avatar) => {
  const formData = new FormData();

  const user = {
    id,
    fullName,
    about,
    avatar,
  };

  const userBlob = new Blob([JSON.stringify(user)], {
    type: "application/json",
  });

  formData.append("updateUserRequest", userBlob);

  if (avatar) {
    formData.append("avatar", avatar);
  }
  const response = await axiosInstance.post("/api/client/users/update-profile", formData);

  return response;
};
