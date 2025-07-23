import { initializeApp } from "firebase/app";
import { getAuth, GoogleAuthProvider, signInWithRedirect, getRedirectResult, GithubAuthProvider } from "firebase/auth";

const firebaseConfig = {
  apiKey: "AIzaSyBdLUbKCKEesZ6z209IOridEks2gEl6lt4",
  authDomain: "quizproject-e9bc5.firebaseapp.com",
  projectId: "quizproject-e9bc5",
  storageBucket: "quizproject-e9bc5.appspot.com", // Đã sửa từ e9bac5 → e9bc5
  messagingSenderId: "81378515734",
  appId: "1:81378515734:web:23d275f9e85e43d3ba0e37",
  measurementId: "G-T5QCVT0CW4",
};

// Khởi tạo Firebase
const app = initializeApp(firebaseConfig);
const auth = getAuth(app);
const provider = new GoogleAuthProvider();
const githubProvier = new GithubAuthProvider();

// Hàm xử lý đăng nhập bằng redirect
export const authWithGoogle = async () => {
  try {
    // 1. Chuyển hướng sang trang đăng nhập Google
    await signInWithRedirect(auth, provider);

    // 2. Sau khi redirect trở về, lấy kết quả
    const result = await getRedirectResult(auth);
    if (result) {
      return result.user; // Trả về user nếu thành công
    }
    return null;
  } catch (error) {
    console.error("Lỗi đăng nhập Google:", error);
    throw error;
  }
};

export const authWithGithub = async () => {
  try {
    // 1. Chuyển hướng sang trang đăng nhập Google
    await signInWithRedirect(auth, githubProvier);

    // 2. Sau khi redirect trở về, lấy kết quả
    const result = await getRedirectResult(auth);
    if (result) {
      return result.user; // Trả về user nếu thành công
    }
    return null;
  } catch (error) {
    console.error("Lỗi đăng nhập Github:", error);
    throw error;
  }
};
