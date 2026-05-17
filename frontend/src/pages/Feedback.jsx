import { useEffect, useState } from "react";
import { request } from "../api/client.js";
import { useAuth } from "../contexts/AuthContext.jsx";

export default function Feedback() {
  const { isAdmin } = useAuth();
  const [reviews, setReviews] = useState([]);
  const [adminFeedback, setAdminFeedback] = useState([]);
  const [adminLoading, setAdminLoading] = useState(false);
  const [adminError, setAdminError] = useState("");
  const [form, setForm] = useState({
    topic: "App",
    tone: "idea",
    allowContact: false,
    reviewId: "",
    message: ""
  });
  const [status, setStatus] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        const data = await request("/reviews");
        setReviews(data || []);
        if (data?.length) {
          setForm((prev) => ({ ...prev, reviewId: data[0].id }));
        } else {
          setForm((prev) => ({ ...prev, reviewId: "" }));
        }
      } catch (err) {
        setError(err.message);
      }
    };

    load();
  }, []);

  useEffect(() => {
    if (!isAdmin) {
      return;
    }

    const loadAdminFeedback = async () => {
      setAdminLoading(true);
      setAdminError("");
      try {
        const data = await request("/feedback");
        setAdminFeedback(data || []);
      } catch (err) {
        setAdminError(err.message);
      } finally {
        setAdminLoading(false);
      }
    };

    loadAdminFeedback();
  }, [isAdmin]);

  const handleChange = (event) => {
    const { name, value, type, checked } = event.target;
    setForm((prev) => ({
      ...prev,
      [name]: type === "checkbox" ? checked : value
    }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setStatus("");
    setError("");

    try {
      await request("/feedback", {
        method: "POST",
        body: JSON.stringify({
          topic: form.topic,
          tone: form.tone,
          allowContact: form.allowContact,
          message: form.message,
          reviewId: form.reviewId || null
        })
      });
      setStatus("Feedback sent. Thanks for helping the station grow.");
      setForm((prev) => ({ ...prev, message: "" }));
      if (isAdmin) {
        const data = await request("/feedback");
        setAdminFeedback(data || []);
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const toggleRead = async (item) => {
    setAdminError("");
    try {
      const updated = await request(`/feedback/${item.id}/read?read=${!item.read}`,
        { method: "PUT" }
      );
      setAdminFeedback((prev) =>
        prev.map((entry) => (entry.id === item.id ? updated : entry))
      );
    } catch (err) {
      setAdminError(err.message);
    }
  };

  return (
    <main className="page">
      <header className="page-header">
        <h1>Feedback loop</h1>
        <p>Let us know what to build next.</p>
      </header>

      <section className="panel">
        <form className="form-grid" onSubmit={handleSubmit}>
          {status && <div className="success">{status}</div>}
          {error && <div className="alert">{error}</div>}
          <div className="form-row">
            <label htmlFor="topic">Feedback topic</label>
            <select id="topic" name="topic" value={form.topic} onChange={handleChange}>
              <option value="App">App experience</option>
              <option value="Album">Album content</option>
              <option value="Review">Review quality</option>
              <option value="Feature">New feature idea</option>
            </select>
          </div>
          <div className="form-row">
            <label>Feedback tone</label>
            <label className="helper">
              <input
                type="radio"
                name="tone"
                value="praise"
                checked={form.tone === "praise"}
                onChange={handleChange}
              />
              Praise
            </label>
            <label className="helper">
              <input
                type="radio"
                name="tone"
                value="bug"
                checked={form.tone === "bug"}
                onChange={handleChange}
              />
              Bug report
            </label>
            <label className="helper">
              <input
                type="radio"
                name="tone"
                value="idea"
                checked={form.tone === "idea"}
                onChange={handleChange}
              />
              Idea
            </label>
          </div>
          <div className="form-row">
            <label htmlFor="reviewId">Related review</label>
            <select
              id="reviewId"
              name="reviewId"
              value={form.reviewId}
              onChange={handleChange}
            >
              <option value="">General feedback</option>
              {reviews.map((review) => (
                <option key={review.id} value={review.id}>
                  {review.title} - {review.albumTitle}
                </option>
              ))}
            </select>
          </div>
          <div className="form-row">
            <label className="helper">
              <input
                type="checkbox"
                name="allowContact"
                checked={form.allowContact}
                onChange={handleChange}
              />
              Allow follow-up about this feedback
            </label>
          </div>
          <div className="form-row">
            <label htmlFor="message">Your message</label>
            <textarea
              id="message"
              name="message"
              required
              value={form.message}
              onChange={handleChange}
              placeholder="Tell us what you want to improve"
            />
          </div>
          <div className="form-actions">
            <button className="button" type="submit">
              Send feedback
            </button>
          </div>
        </form>
      </section>

      {isAdmin && (
        <section className="panel">
          <div className="toolbar">
            <div>
              <h2>Feedback inbox</h2>
              <p className="helper">Mark items as read once reviewed.</p>
            </div>
          </div>
          {adminError && <div className="alert">{adminError}</div>}
          {adminLoading ? (
            <div className="panel loading">Loading feedback...</div>
          ) : (
            <div className="feedback-list">
              {adminFeedback.map((item) => (
                <div key={item.id} className="card feedback-card">
                  <div className="feedback-header">
                    <div>
                      <strong>{item.topic}</strong>
                      <small className="helper">{item.tone}</small>
                    </div>
                    <span
                      className={
                        item.read ? "feedback-status read" : "feedback-status unread"
                      }
                    >
                      {item.read ? "Read" : "Unread"}
                    </span>
                  </div>
                  <p className="helper">{item.message}</p>
                  <div className="feedback-meta">
                    <span>{item.username}</span>
                    <span>{item.createdAt?.slice(0, 10)}</span>
                    <span>{item.allowContact ? "Contact allowed" : "No contact"}</span>
                    <span>
                      {item.reviewTitle
                        ? `Review: ${item.reviewTitle}`
                        : "General"}
                    </span>
                  </div>
                  <div className="form-actions">
                    <button
                      className="button secondary"
                      type="button"
                      onClick={() => toggleRead(item)}
                    >
                      {item.read ? "Mark unread" : "Mark read"}
                    </button>
                  </div>
                </div>
              ))}
              {!adminFeedback.length && !adminError && (
                <p className="helper">No feedback yet.</p>
              )}
            </div>
          )}
        </section>
      )}
    </main>
  );
}
