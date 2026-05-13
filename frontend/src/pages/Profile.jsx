import { useEffect, useState } from "react";
import { request } from "../api/client.js";
import { useAuth } from "../contexts/AuthContext.jsx";

export default function Profile() {
  const { user, refresh } = useAuth();
  const [reviews, setReviews] = useState([]);
  const [reviewsError, setReviewsError] = useState("");
  const [reviewsLoading, setReviewsLoading] = useState(false);
  const [form, setForm] = useState({
    displayName: "",
    bio: "",
    avatarUrl: ""
  });
  const [status, setStatus] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    if (user) {
      setForm({
        displayName: user.displayName || "",
        bio: user.bio || "",
        avatarUrl: user.avatarUrl || ""
      });
    }
  }, [user]);

  useEffect(() => {
    if (!user?.userId) {
      return;
    }

    const loadReviews = async () => {
      setReviewsLoading(true);
      setReviewsError("");
      try {
        const data = await request(`/reviews/user/${user.userId}`);
        setReviews(data || []);
      } catch (err) {
        setReviewsError(err.message);
      } finally {
        setReviewsLoading(false);
      }
    };

    loadReviews();
  }, [user?.userId]);

  const handleChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setStatus("");
    setError("");

    try {
      await request("/users/me", {
        method: "PUT",
        body: JSON.stringify(form)
      });
      await refresh();
      setStatus("Profile updated.");
    } catch (err) {
      setError(err.message);
    }
  };

  if (!user) {
    return (
      <main className="page">
        <div className="panel loading">Loading profile...</div>
      </main>
    );
  }

  return (
    <main className="page">
      <header className="page-header">
        <h1>Your profile</h1>
        <p>Update how you appear in the review community.</p>
      </header>

      <section className="panel">
        <div className="panel-grid">
          <div className="card">
            <strong>{user.username}</strong>
            <small>{user.email}</small>
            <span className="tag">Reviews {user.reviewCount}</span>
          </div>
          <div className="card">
            <strong>{user.followerCount}</strong>
            <small>Followers</small>
          </div>
          <div className="card">
            <strong>{user.followingCount}</strong>
            <small>Following</small>
          </div>
        </div>
      </section>

      <section className="panel">
        <form className="form-grid" onSubmit={handleSubmit}>
          {status && <div className="success">{status}</div>}
          {error && <div className="alert">{error}</div>}
          <div className="form-row">
            <label htmlFor="displayName">Display name</label>
            <input
              id="displayName"
              name="displayName"
              value={form.displayName}
              onChange={handleChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="bio">Bio</label>
            <textarea id="bio" name="bio" value={form.bio} onChange={handleChange} />
          </div>
          <div className="form-row">
            <label htmlFor="avatarUrl">Avatar URL</label>
            <input
              id="avatarUrl"
              name="avatarUrl"
              value={form.avatarUrl}
              onChange={handleChange}
            />
          </div>
          <div className="form-actions">
            <button className="button" type="submit">
              Save changes
            </button>
          </div>
        </form>
      </section>

      <section className="panel">
        <div className="toolbar">
          <div>
            <h2>My reviews</h2>
            <p className="helper">A quick look at your latest takes.</p>
          </div>
        </div>
        {reviewsError && <div className="alert">{reviewsError}</div>}
        {reviewsLoading ? (
          <div className="panel loading">Loading reviews...</div>
        ) : (
          <div className="review-list">
            {reviews.map((review) => (
              <div key={review.id} className="card review-card">
                <div className="review-header">
                  <strong>{review.title}</strong>
                  <span className="tag">{review.rating}/10</span>
                </div>
                <p className="helper">{review.content}</p>
                <div className="review-meta">
                  <span>{review.albumTitle}</span>
                  <span>{review.createdAt?.slice(0, 10)}</span>
                  <span>{review.likeCount} likes</span>
                  <span>{review.commentCount} comments</span>
                </div>
              </div>
            ))}
            {reviews.length === 0 && !reviewsError && (
              <p className="helper">No reviews yet. Start with your favorite album.</p>
            )}
          </div>
        )}
      </section>
    </main>
  );
}
