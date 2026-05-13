import { useEffect, useState } from "react";
import { request } from "../api/client.js";
import { useAuth } from "../contexts/AuthContext.jsx";
import { Link } from "react-router-dom";

export default function Home() {
  const { token } = useAuth();
  const [reviews, setReviews] = useState([]);
  const [error, setError] = useState("");

  useEffect(() => {
    if (!token) {
      return;
    }

    const load = async () => {
      try {
        const data = await request("/reviews/feed");
        setReviews(data || []);
      } catch (err) {
        setError(err.message);
      }
    };

    load();
  }, [token]);

  return (
    <main className="page">
      <header className="page-header">
        <h1>Album Review Station</h1>
        <p>A social feed for albums, reviews, and quick discoveries.</p>
      </header>

      {!token && (
        <section className="panel hero">
          <h2>Join the Album Review Station community</h2>
          <p className="helper">
            Follow your friends' reviews, share your thoughts, and build your own archive of
            favorite albums.
          </p>
          <div className="hero-actions">
            <Link className="button" to="/login">
              Login
            </Link>
            <Link className="button secondary" to="/register">
              Create Account
            </Link>
          </div>
        </section>
      )}

      {token && (
        <section className="panel">
          <div className="toolbar">
            <div>
              <h2>Review Timeline</h2>
              <p className="helper">Latest community activity in a social feed.</p>
            </div>
          </div>
          {error && <div className="alert">{error}</div>}
          <div className="feed">
            {reviews.map((review) => {
              const username = review.username || "guest";
              const handle = `@${username}`;
              const albumTitle = review.albumTitle || "Unknown album";
              const reviewTitle = review.title || albumTitle;
              const rating = review.rating ?? "-";
              const content = review.content || "No description yet.";

              return (
                <article key={review.id} className="feed-card card">
                  <div className="feed-header">
                    <div className="feed-user">
                      <div className="avatar">{username.charAt(0).toUpperCase()}</div>
                      <div>
                        <strong>{handle}</strong>
                        <span className="feed-meta">Listening: {albumTitle}</span>
                      </div>
                    </div>
                    <span className="rating-tag">{rating}/10</span>
                  </div>
                  <div>
                    <h3 className="feed-title">{reviewTitle}</h3>
                    <p className="helper">{content}</p>
                  </div>
                </article>
              );
            })}
            {reviews.length === 0 && !error && (
              <div className="feed-empty">
                <p className="helper">
                  Your feed is empty. Discover new people in Users and follow them to see reviews
                  here.
                </p>
                <Link className="button secondary" to="/users">
                  Go to Users
                </Link>
              </div>
            )}
          </div>
        </section>
      )}
    </main>
  );
}
