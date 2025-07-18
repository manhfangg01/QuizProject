import axios from "axios";
import axiosCustom from "../utils/axiosCustomize";
export const callRegister = (fullName, email, password, confirmPassword) => {
  return axiosCustom.post("/api/auth/signup", { fullName, email, password, confirmPassword });
};

const instance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

export const callLogin = (username, password) => {
  return axiosCustom.post("/api/auth/login", { username, password });
};

export const callFetchAccount = () => {
  return axiosCustom.get("/api/auth/account");
};

export const callRefreshToken = () => {
  return axiosCustom.get("/api/auth/refresh");
};

export const callLogout = () => {
  return instance.post("/api/auth/logout", null, {
    headers: {
      Authorization: "Bearer " + window.localStorage.getItem("accessToken"),
    },
  });
};

// forgot-password

export const callCheckEmail = (username) => {
  return axiosCustom.post("http://localhost:8080/api/auth/request-reset-link", {
    username,
  });
};

export const callResetPassword = (resetToken, newPassword) => {
  return axiosCustom.post("http://localhost:8080/api/auth/reset-password", {
    resetToken,
    newPassword,
  });
};
