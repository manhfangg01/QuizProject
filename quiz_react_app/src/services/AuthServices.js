import axiosCustom from "../utils/axiosCustomize";
const postSignUp = async (fullName, email, password, confirmPassword) => {
  const response = await axiosCustom.post("http://localhost:8080/api/auth/signup", {
    fullName, // dùng biến được truyền vào
    email,
    password,
    confirmPassword,
  });

  return response;
};
export { postSignUp };

const postLogin = async (username, password) => {
  const response = await axiosCustom.post("http://localhost:8080/api/auth/login", {
    username,
    password,
  });

  return response;
};
export { postLogin };
