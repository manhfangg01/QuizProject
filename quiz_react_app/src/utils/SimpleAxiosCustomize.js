import axios from "axios";

// Tạo instance cơ bản
const simpleAxios = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true, // Cho phép gửi cookie
});

let isRefreshing = false;
let refreshSubscribers = [];

// Hàm xử lý refresh token
const refreshToken = async () => {
  try {
    const response = await simpleAxios.get("/api/auth/refresh");
    return response.data.accessToken;
  } catch (error) {
    console.error("Refresh token failed:", error);
    return null;
  }
};

// Xử lý các request bị lỗi 401
const processQueue = (error, token) => {
  refreshSubscribers.forEach((subscriber) => {
    if (error) {
      subscriber("");
    } else {
      subscriber(token || "");
    }
  });
  refreshSubscribers = [];
};

// Interceptor request
simpleAxios.interceptors.request.use((config) => {
  const token = localStorage.getItem("accessToken");
  if (token && !config.url.includes("/auth/")) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

// Interceptor response
simpleAxios.interceptors.response.use(
  (response) => response.data,
  async (error) => {
    const originalRequest = error.config;

    // Nếu lỗi 401 và không phải là request refresh token
    if (error.response && error.response.status === 401 && !originalRequest._retry && !originalRequest.url.includes("/auth/refresh")) {
      if (isRefreshing) {
        // Nếu đang refresh thì đưa vào hàng đợi
        return new Promise((resolve) => {
          refreshSubscribers.push((newToken) => {
            originalRequest.headers.Authorization = `Bearer ${newToken}`;
            resolve(simpleAxios(originalRequest));
          });
        });
      }

      originalRequest._retry = true;
      isRefreshing = true;

      try {
        const newToken = await refreshToken();
        if (newToken) {
          localStorage.setItem("accessToken", newToken);
          originalRequest.headers.Authorization = `Bearer ${newToken}`;
          processQueue(null, newToken);
          return simpleAxios(originalRequest);
        }
      } catch (refreshError) {
        processQueue(refreshError);
        localStorage.removeItem("accessToken");
        // Redirect đến trang login khi refresh thất bại
        window.location.href = "/login";
        return Promise.reject(refreshError);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error.response ? error.response.data : error);
  }
);

export default simpleAxios;
