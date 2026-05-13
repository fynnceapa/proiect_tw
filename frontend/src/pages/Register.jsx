import { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { useAuth } from "../contexts/AuthContext.jsx";

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({
    username: "",
    email: "",
    password: "",
    confirmPassword: "",
    displayName: "",
    bio: "",
    avatarUrl: ""
  });
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setError("");

    if (form.password !== form.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);
    try {
      await register({
        email: form.email,
        username: form.username,
        password: form.password,
        displayName: form.displayName,
        bio: form.bio,
        avatarUrl: form.avatarUrl
      });
      navigate("/profile");
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <main className="page">
      <header className="page-header">
        <h1>Create account</h1>
        <p>Register with your username, email, and profile details.</p>
      </header>

      <section className="panel">
        <form className="form-grid" onSubmit={handleSubmit}>
          {error && <div className="alert">{error}</div>}
          <div className="form-row">
            <label htmlFor="username">Username</label>
            <input
              id="username"
              name="username"
              required
              value={form.username}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="email">Email</label>
            <input
              id="email"
              name="email"
              type="email"
              required
              value={form.email}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="password">Password</label>
            <input
              id="password"
              name="password"
              type="password"
              required
              value={form.password}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="confirmPassword">Confirm password</label>
            <input
              id="confirmPassword"
              name="confirmPassword"
              type="password"
              required
              value={form.confirmPassword}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="displayName">Display name</label>
            <input
              id="displayName"
              name="displayName"
              value={form.displayName}
              onChange={handleChange}
              placeholder="Your public name"
            />
          </div>
          <div className="form-row">
            <label htmlFor="bio">Short bio</label>
            <textarea
              id="bio"
              name="bio"
              value={form.bio}
              onChange={handleChange}
              placeholder="Tell us about your music taste"
            />
          </div>
          <div className="form-row">
            <label htmlFor="avatarUrl">Avatar URL</label>
            <input
              id="avatarUrl"
              name="avatarUrl"
              value={form.avatarUrl}
              onChange={handleChange}
              placeholder="https://..."
            />
          </div>
          <div className="form-actions">
            <button className="button" type="submit" disabled={loading}>
              {loading ? "Creating..." : "Register"}
            </button>
            <Link className="button secondary" to="/login">
              Back to login
            </Link>
          </div>
        </form>
      </section>
    </main>
  );
}
