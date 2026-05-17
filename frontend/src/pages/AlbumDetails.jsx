import { useEffect, useState } from "react";
import { Link, useParams } from "react-router-dom";
import { request } from "../api/client.js";
import { useAuth } from "../contexts/AuthContext.jsx";

const sampleSize = 24;

const mixWithWhite = (value, amount) =>
  Math.round(value + (255 - value) * amount);

const getLuminance = ({ r, g, b }) =>
  0.2126 * r + 0.7152 * g + 0.0722 * b;

const getAverageColor = (image) => {
  const canvas = document.createElement("canvas");
  canvas.width = sampleSize;
  canvas.height = sampleSize;
  const context = canvas.getContext("2d", { willReadFrequently: true });

  if (!context) {
    return null;
  }

  context.drawImage(image, 0, 0, sampleSize, sampleSize);
  const { data } = context.getImageData(0, 0, sampleSize, sampleSize);
  let r = 0;
  let g = 0;
  let b = 0;
  let count = 0;

  for (let i = 0; i < data.length; i += 4) {
    const alpha = data[i + 3];
    if (alpha < 128) {
      continue;
    }
    r += data[i];
    g += data[i + 1];
    b += data[i + 2];
    count += 1;
  }

  if (!count) {
    return null;
  }

  return {
    r: Math.round(r / count),
    g: Math.round(g / count),
    b: Math.round(b / count)
  };
};

export default function AlbumDetails() {
  const { id } = useParams();
  const { user } = useAuth();
  const [album, setAlbum] = useState(null);
  const [reviews, setReviews] = useState([]);
  const [error, setError] = useState("");
  const [formError, setFormError] = useState("");
  const [formStatus, setFormStatus] = useState("");
  const [submitting, setSubmitting] = useState(false);
  const [form, setForm] = useState({
    title: "",
    rating: "",
    content: ""
  });
  const [loading, setLoading] = useState(true);
  const [pageTint, setPageTint] = useState("");
  const [pageInk, setPageInk] = useState("");
  const [pageMuted, setPageMuted] = useState("");

  const loadAlbum = async () => {
    const albumData = await request(`/albums/${id}`);
    setAlbum(albumData);
  };

  const loadReviews = async () => {
    const reviewData = await request(`/reviews/album/${id}`);
    setReviews(reviewData || []);
  };

  useEffect(() => {
    const load = async () => {
      setLoading(true);
      setError("");
      try {
        await Promise.all([loadAlbum(), loadReviews()]);
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };

    load();
  }, [id]);

  useEffect(() => {
    if (!album?.coverImageUrl) {
      setPageTint("");
      return;
    }

    let cancelled = false;
    const image = new Image();
    image.crossOrigin = "anonymous";
    image.decoding = "async";
    image.src = album.coverImageUrl;

    image.onload = () => {
      if (cancelled) {
        return;
      }

      try {
        const average = getAverageColor(image);
        if (!average) {
          setPageTint("");
          setPageInk("");
          setPageMuted("");
          return;
        }

        const softened = {
          r: mixWithWhite(average.r, 0.55),
          g: mixWithWhite(average.g, 0.55),
          b: mixWithWhite(average.b, 0.55)
        };
        const luminance = getLuminance(softened);
        const useLightText = luminance < 140;
        setPageTint(`rgb(${softened.r}, ${softened.g}, ${softened.b})`);
        setPageInk(useLightText ? "#ffffff" : "#111111");
        setPageMuted(
          useLightText ? "rgba(255, 255, 255, 0.72)" : "#6e6e6e"
        );
      } catch (err) {
        setPageTint("");
        setPageInk("");
        setPageMuted("");
      }
    };

    image.onerror = () => {
      if (!cancelled) {
        setPageTint("");
        setPageInk("");
        setPageMuted("");
      }
    };

    return () => {
      cancelled = true;
    };
  }, [album?.coverImageUrl]);

  useEffect(() => {
    if (pageTint) {
      document.body.style.backgroundColor = pageTint;
      if (pageInk) {
        document.body.style.setProperty("--ink", pageInk);
      }
      if (pageMuted) {
        document.body.style.setProperty("--muted", pageMuted);
      }
      return () => {
        document.body.style.backgroundColor = "";
        document.body.style.removeProperty("--ink");
        document.body.style.removeProperty("--muted");
      };
    }

    document.body.style.backgroundColor = "";
    document.body.style.removeProperty("--ink");
    document.body.style.removeProperty("--muted");
    return () => {
      document.body.style.backgroundColor = "";
      document.body.style.removeProperty("--ink");
      document.body.style.removeProperty("--muted");
    };
  }, [pageTint, pageInk, pageMuted]);

  const handleFormChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSubmit = async (event) => {
    event.preventDefault();
    setFormError("");
    setFormStatus("");
    setSubmitting(true);

    try {
      await request("/reviews", {
        method: "POST",
        body: JSON.stringify({
          title: form.title,
          rating: Number(form.rating),
          content: form.content,
          albumId: id
        })
      });
      setForm({ title: "", rating: "", content: "" });
      setFormStatus("Review added.");
      await loadReviews();
      await loadAlbum();
    } catch (err) {
      setFormError(err.message);
    } finally {
      setSubmitting(false);
    }
  };

  const handleToggleLike = async (review) => {
    if (review.userId === user?.userId) {
      return;
    }

    const nextLiked = !review.likedByCurrentUser;
    setReviews((prev) =>
      prev.map((item) =>
        item.id === review.id
          ? {
              ...item,
              likedByCurrentUser: nextLiked,
              likeCount: item.likeCount + (nextLiked ? 1 : -1)
            }
          : item
      )
    );

    try {
      await request(`/reviews/${review.id}/like`, {
        method: nextLiked ? "POST" : "DELETE"
      });
    } catch (err) {
      setError(err.message);
      await loadReviews();
    }
  };

  if (loading) {
    return (
      <main className="page album-page">
        <div className="panel loading">Loading album...</div>
      </main>
    );
  }

  if (!album) {
    return (
      <main className="page album-page">
        <section className="panel">
          <p className="helper">Album not found.</p>
          <Link className="button" to="/albums">
            Back to albums
          </Link>
        </section>
      </main>
    );
  }

  return (
    <main className="page album-page">
      <header className="page-header">
        <h1>{album.title}</h1>
        <p>{album.artist?.name || "Unknown artist"}</p>
      </header>

      <section className="panel">
        <div className="album-detail">
          <div
            className="album-cover large"
            style={
              album.coverImageUrl
                ? { backgroundImage: `url(${album.coverImageUrl})` }
                : undefined
            }
          >
            {!album.coverImageUrl && (
              <span className="album-cover-fallback">No cover</span>
            )}
          </div>
          <div className="album-detail-info">
            <div className="card">
              <strong>Release year</strong>
              <small>{album.releaseYear || "Unknown"}</small>
            </div>
            <div className="card">
              <strong>Genres</strong>
              <small>
                {(album.genres || []).map((genre) => genre.name).join(", ") ||
                  "-"}
              </small>
            </div>
            <div className="card">
              <strong>Average rating</strong>
              <small>{album.averageRating?.toFixed?.(1) || "-"}</small>
            </div>
          </div>
        </div>
      </section>

      <section className="panel">
        <div className="toolbar">
          <div>
            <h2>Reviews</h2>
            <p className="helper">{reviews.length} entries on this album.</p>
          </div>
          <Link className="button secondary" to="/reviews">
            Write a review
          </Link>
        </div>
        <form className="form-grid review-form" onSubmit={handleSubmit}>
          {formStatus && <div className="success">{formStatus}</div>}
          {formError && <div className="alert">{formError}</div>}
          <div className="form-row">
            <label htmlFor="title">Title</label>
            <input
              id="title"
              name="title"
              required
              value={form.title}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="rating">Rating (1-10)</label>
            <input
              id="rating"
              name="rating"
              type="number"
              min="1"
              max="10"
              required
              value={form.rating}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="content">Content</label>
            <textarea
              id="content"
              name="content"
              required
              value={form.content}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-actions">
            <button className="button" type="submit" disabled={submitting}>
              {submitting ? "Saving..." : "Post review"}
            </button>
          </div>
        </form>
        {error && <div className="alert">{error}</div>}
        <div className="review-list">
          {reviews.map((review) => (
            <div key={review.id} className="card review-card">
              <div className="review-header">
                <strong>{review.title}</strong>
                <div className="review-actions">
                  <span className="tag">{review.rating}/10</span>
                  <button
                    className="button ghost"
                    type="button"
                    disabled={review.userId === user?.userId}
                    onClick={() => handleToggleLike(review)}
                  >
                    {review.likedByCurrentUser ? "Unlike" : "Like"}
                  </button>
                </div>
              </div>
              <p className="helper">{review.content}</p>
              <div className="review-meta">
                <span>{review.username}</span>
                <span>{review.createdAt?.slice(0, 10)}</span>
                <span>{review.likeCount} likes</span>
                <span>{review.commentCount} comments</span>
              </div>
            </div>
          ))}
          {reviews.length === 0 && !error && (
            <p className="helper">No reviews yet. Be the first.</p>
          )}
        </div>
      </section>
    </main>
  );
}
