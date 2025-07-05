import axios from "axios";
import { Mutex } from "async-mutex";

import { notification } from "antd";

const instance = axios.create({
  baseURL: "http://localhost:8080",
  withCredentials: true,
});

const mutex = new Mutex();
const NO_RETRY_HEADER = "x-no-retry";

const handleRefreshToken = async () => {
  return await mutex.runExclusive(async () => {
    const res = await instance.get("/api/auth/refresh");
    if (res && res.data) return res.data.accessToken;
    else return null;
  });
};

const EXCLUDED_URLS = ["/api/auth/login", "/api/auth/register", "/api/auth/refresh"];

instance.interceptors.request.use(function (config) {
  const shouldSkipAuth = EXCLUDED_URLS.some((url) => config.url?.includes(url));

  if (!shouldSkipAuth) {
    if (typeof window !== "undefined" && window && window.localStorage && window.localStorage.getItem("accessToken")) {
      config.headers.Authorization = "Bearer " + window.localStorage.getItem("accessToken");
    }
  }
  if (!config.headers.Accept && config.headers["Content-Type"]) {
    config.headers.Accept = "application/json";
    config.headers["Content-Type"] = "application/json; charset=utf-8";
  }
  return config;
});

/**
 * Handle all responses. It is possible to add handlers
 * for requests, but it is omitted here for brevity.
 */
instance.interceptors.response.use(
  (res) => res.data,
  async (error) => {
    if (error.config && error.response && +error.response.status === 401 && error.config.url !== "/api/auth/login" && !error.config.headers[NO_RETRY_HEADER]) {
      const accessToken = await handleRefreshToken();
      error.config.headers[NO_RETRY_HEADER] = "true";
      if (accessToken) {
        error.config.headers["Authorization"] = `Bearer ${accessToken}`;
        localStorage.setItem("accessToken", accessToken);
        return instance.request(error.config);
      }
    }

    if (
      error.config &&
      error.response &&
      +error.response.status === 400 &&
      error.config.url === "/api/auth/refresh" &&
      typeof window !== "undefined" &&
      window.location &&
      window.location.pathname.startsWith("/admin")
    ) {
      notification.error({
        message: "Refresh token thất bại",
        description: error?.response?.data?.error ?? "Vui lòng đăng nhập lại.",
      });
    }

    if (+error.response.status === 403) {
      notification.error({
        message: error?.response?.data?.message ?? "",
        description: error?.response?.data?.error ?? "",
      });
    }

    return error?.response?.data ?? Promise.reject(error);
  }
);

/**
 * Replaces main `axios` instance with the custom-one.
 *
 * @param cfg - Axios configuration object.
 * @returns A promise object of a response of the HTTP request with the 'data' object already
 * destructured.
 */
// const axios = <T>(cfg: AxiosRequestConfig) => instance.request<any, T>(cfg);

// export default axios;

export default instance;
