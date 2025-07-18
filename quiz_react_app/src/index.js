import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Admin from "./components/admin/Admin";
import Home from "./components/home/Home";
import ManageUsers from "../src/components/admin/content/user/ManageUsers";
import ManageOptions from "../src/components/admin/content/option/ManageOptions";
import ManageQuizzes from "../src/components/admin/content/quiz/ManageQuizzes";
import ManageQuestions from "../src/components/admin/content/question/ManageQuestions";
import ManageResults from "../src/components/admin/content/result/ManageResults";
import Dashboard from "./components/admin/dashboard/Dashboard";
import Login from "./components/auth/Login";
import Signup from "./components/auth/Signup";
import CheckingEmail from "./components/auth/forgot-password/CheckingEmail";
import ResetPassword from "./components/auth/forgot-password/ResetPassword";
import Profile from "./components/profile/Profile";
import Setting from "./components/profile/Setting";
import Unauthorized from "./components/auth/unauthorized/Unauthorized";
import AdminProfile from "./components/admin/profile/AdminProfile";
import AdminSetting from "./components/admin/profile/AdminSetting";
import ResultDetail from "./components/details/ResultDetail.js";
import StatisticsPage from "./components/details/StatisticsPage.js";
import LibraryQuizzes from "./components/quizzes/LibraryQuizzes.js";
import DoQuiz from "./components/play/DoQuiz.js";
import Unauthenticated from "./components/auth/unauthorized/Unauthenticated.js";
import ProtectedRoute from "./components/auth/ProtectedRoute.js";

const root = ReactDOM.createRoot(document.getElementById("root"));
const isAuthenticated = !!localStorage.getItem("accessToken");
const user = JSON.parse(localStorage.getItem("user"));
const userRole = user?.role || "GUEST";

root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          {/* Public routes */}
          <Route index element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<Signup />} />
          <Route path="forgot-password" element={<CheckingEmail />} />
          <Route path="reset-password" element={<ResetPassword />} />
          <Route path="unauthorized" element={<Unauthorized />} />
          <Route path="unauthenticated" element={<Unauthenticated />} />
          <Route path="/quizzes" element={<LibraryQuizzes />} />
          {/* Protected routes for logged-in users (USER or ADMIN) */}
          <Route
            path="my-account"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["USER", "ADMIN"]}>
                <Profile />
              </ProtectedRoute>
            }
          ></Route>
          <Route
            path="my-account/setting"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["USER", "ADMIN"]}>
                <Setting />
              </ProtectedRoute>
            }
          />

          {/* Quiz pages (logged-in required) */}
          <Route
            path="/results/:id"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["USER", "ADMIN"]}>
                <ResultDetail />
              </ProtectedRoute>
            }
          />
          <Route
            path="/results/statistics"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["USER", "ADMIN"]}>
                <StatisticsPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/do-quiz/:id"
            element={
              <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["USER", "ADMIN"]}>
                <DoQuiz />
              </ProtectedRoute>
            }
          />
        </Route>

        {/* Admin routes */}
        <Route
          path="/admins"
          element={
            <ProtectedRoute isAuthenticated={isAuthenticated} userRole={userRole} allowedRoles={["ADMIN"]}>
              <Admin />
            </ProtectedRoute>
          }
        >
          <Route index element={<Dashboard />} />
          <Route path="manage-users" element={<ManageUsers />} />
          <Route path="manage-options" element={<ManageOptions />} />
          <Route path="manage-quizzes" element={<ManageQuizzes />} />
          <Route path="manage-questions" element={<ManageQuestions />} />
          <Route path="manage-results" element={<ManageResults />} />
          <Route path="manage-profile" element={<AdminProfile />} />
          <Route path="manage-profile/setting" element={<AdminSetting />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);
// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
