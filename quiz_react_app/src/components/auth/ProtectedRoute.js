import { Navigate } from "react-router-dom";

const ProtectedRoute = ({ isAuthenticated, allowedRoles = [], userRole, children }) => {
  if (!isAuthenticated) {
    return <Navigate to="/unauthenticated" replace />;
  }

  if (allowedRoles.length > 0 && !allowedRoles.includes(userRole)) {
    return <Navigate to="/unauthorized" replace />;
  }

  return children;
};

export default ProtectedRoute;
