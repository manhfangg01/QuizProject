import React from "react";
import ReactDOM from "react-dom/client";
import "./index.css";
import App from "./App";
import reportWebVitals from "./reportWebVitals";
import "bootstrap/dist/css/bootstrap.min.css";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import Admin from "./components/admin/Admin";
import User from "./components/user/User";
import Home from "./components/home/Home";
import ManageUsers from "../src/components/admin/content/user/ManageUsers";
import ManageOptions from "../src/components/admin/content/ManageOptions";
import ManageQuizzes from "../src/components/admin/content/ManageQuizzes";
import ManageQuestions from "../src/components/admin/content/ManageQuestions";
import ManageResults from "../src/components/admin/content/ManageResults";
import Dashboard from "./components/admin/dashboard/Dashboard";
import Login from "./components/auth/Login";
import Signup from "./components/auth/Signup";
import CheckingEmail from "./components/auth/forgot-password/CheckingEmail";
import ResetPassword from "./components/auth/forgot-password/ResetPassword";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<App />}>
          <Route path="users" element={<User />} />
          <Route index element={<Home />} />
          <Route path="login" element={<Login />} />
          <Route path="signup" element={<Signup />} />
          <Route path="forgot-password" element={<CheckingEmail />} />
          <Route path="reset-password" element={<ResetPassword />} />
        </Route>

        <Route path="/admins" element={<Admin />}>
          <Route index element={<Dashboard />} />
          <Route path="manage-users" element={<ManageUsers />} />
          <Route path="manage-options" element={<ManageOptions />} />
          <Route path="manage-quizzes" element={<ManageQuizzes />} />
          <Route path="manage-questions" element={<ManageQuestions />} />
          <Route path="manage-results" element={<ManageResults />} />
        </Route>
      </Routes>
    </BrowserRouter>
  </React.StrictMode>
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
