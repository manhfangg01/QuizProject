import { useEffect, useState } from "react";
const DarkModeToggle = () => {
  const [darkMode, setDarkMode] = useState(() => {
    return localStorage.getItem("theme") === "dark";
  });

  // Gán theme vào <body>
  useEffect(() => {
    document.body.setAttribute("data-bs-theme", darkMode ? "dark" : "light");
    localStorage.setItem("theme", darkMode ? "dark" : "light");
  }, [darkMode]);

  return (
    <button onClick={() => setDarkMode(!darkMode)} className="btn btn-outline-secondary">
      {darkMode ? "☀️ Light Mode" : "🌙 Dark Mode"}
    </button>
  );
};
export default DarkModeToggle;
