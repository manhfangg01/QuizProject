import axiosCustom from "../utils/axiosCustomize";

const postCreateNewUser = async (email, password, fullName, role, imageFile) => {
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

  const token = localStorage.getItem("accessToken");
  if (!token) {
    throw new Error("Vui lòng đăng nhập trước khi thực hiện thao tác này!");
  }

  const response = await axiosCustom.post("/api/admin/users/create", formData, {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response;
};

const getAllUsersService = async () => {
  const token = localStorage.getItem("accessToken");
  if (!token) {
    throw new Error("Vui lòng đăng nhập trước khi thực hiện thao tác này!");
  }
  const response = await axiosCustom.get("/api/admin/users/fetch", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response;
};

export { postCreateNewUser, getAllUsersService };
