import { NavLink, useNavigate } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext.jsx";

export default function NavBar() {
  const { token, user, logout } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  return (
    <nav>
      <div className="nav-brand">
        <strong>Album Review Station</strong>
      </div>
      <div className="nav-links">
        <NavLink to="/">Home (Feed)</NavLink>
        <NavLink to="/albums">Catalog</NavLink>
        <NavLink to="/artists">Artists</NavLink>
        <NavLink to="/users">Users</NavLink>
        <NavLink to="/feedback">Feedback</NavLink>
      </div>
      <div className="nav-actions">
        {token && user ? (
          <>
            <NavLink to="/profile" className="badge">
              @{user.username}
            </NavLink>
            <button type="button" className="button" onClick={handleLogout}>
              Logout
            </button>
          </>
        ) : (
          <>
            <NavLink to="/login" className="button secondary">
              Login
            </NavLink>
            <NavLink to="/register" className="button">
              Create Account
            </NavLink>
          </>
        )}
      </div>
    </nav>
  );
}
