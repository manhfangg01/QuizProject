import axios from "axios";

const instance = axios.create({
  baseURL: "http://localhost:8080",
});

instance.interceptors.request.use(
  function (config) {
    return config;
  },
  function (error) {
    return Promise.reject(error);
  }
);

// Add a response interceptor
instance.interceptors.response.use(
  function (response) {
    // Do something before senting response
    console.log(">>> from interceptor", response);
    return response && response.data ? response.data : response;
  },
  function (error) {
    // when error orcure flow go here
    return Promise.reject(error);
  }
);
export default instance;
