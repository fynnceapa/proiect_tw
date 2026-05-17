import { useEffect, useMemo, useState } from "react";
import { request } from "../api/client.js";
import Modal from "../components/Modal.jsx";
import ConfirmDialog from "../components/ConfirmDialog.jsx";
import Pagination from "../components/Pagination.jsx";
import { useAuth } from "../contexts/AuthContext.jsx";

const emptyForm = {
  title: "",
  content: "",
  rating: "",
  albumId: ""
};

export default function Reviews() {
  const [reviews, setReviews] = useState([]);
  const [albums, setAlbums] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [error, setError] = useState("");
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const { user, isAdmin } = useAuth();

  const pageSize = 6;

  const loadReviews = async () => {
    setError("");
    try {
      const data = await request("/reviews");
      setReviews(data || []);
    } catch (err) {
      setError(err.message);
    }
  };

  const loadAlbums = async () => {
    try {
      const data = await request("/albums");
      setAlbums(data || []);
    } catch (err) {
      setError(err.message);
    }
  };

  useEffect(() => {
    loadAlbums();
  }, []);

  useEffect(() => {
    loadReviews();
  }, [user?.userId]);

  const filteredReviews = useMemo(() => {
    if (!search.trim()) {
      return reviews;
    }
    const query = search.toLowerCase();
    return reviews.filter((review) => {
      return (
        review.title?.toLowerCase().includes(query) ||
        review.albumTitle?.toLowerCase().includes(query) ||
        review.username?.toLowerCase().includes(query)
      );
    });
  }, [reviews, search]);

  const pagedReviews = useMemo(() => {
    const start = (page - 1) * pageSize;
    return filteredReviews.slice(start, start + pageSize);
  }, [filteredReviews, page]);

  const totalPages = Math.max(1, Math.ceil(filteredReviews.length / pageSize));

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (review) => {
    setEditing(review);
    setForm({
      title: review.title || "",
      content: review.content || "",
      rating: review.rating || "",
      albumId: review.albumId || ""
    });
    setModalOpen(true);
  };

  const handleFormChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const handleSave = async (event) => {
    event.preventDefault();
    setError("");
    const payload = {
      title: form.title,
      content: form.content,
      rating: Number(form.rating),
      albumId: form.albumId
    };

    try {
      if (editing) {
        await request(`/reviews/${editing.id}`, {
          method: "PUT",
          body: JSON.stringify(payload)
        });
      } else {
        await request("/reviews", {
          method: "POST",
          body: JSON.stringify(payload)
        });
      }
      setModalOpen(false);
      await loadReviews();
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!confirmDelete) {
      return;
    }

    const deletedId = confirmDelete.id;
    setConfirmDelete(null);
    setReviews((prev) => prev.filter((review) => review.id !== deletedId));

    try {
      await request(`/reviews/${deletedId}`, { method: "DELETE" });
      await loadReviews();
    } catch (err) {
      setError(err.message);
      await loadReviews();
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

  return (
    <main className="page">
      <header className="page-header">
        <h1>Reviews</h1>
        <p>Write, edit, and keep track of reviews across albums.</p>
      </header>

      <section className="panel">
        <div className="toolbar">
          <input
            type="search"
            placeholder="Search title, album, or author"
            value={search}
            onChange={(event) => {
              setSearch(event.target.value);
              setPage(1);
            }}
          />
          <button className="button" type="button" onClick={openCreate}>
            Add review
          </button>
        </div>
        {error && <div className="alert">{error}</div>}
        <table className="table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Album</th>
              <th>Rating</th>
              <th>Likes</th>
              <th>Author</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {pagedReviews.map((review) => (
              <tr key={review.id}>
                <td>{review.title}</td>
                <td>{review.albumTitle}</td>
                <td>{review.rating}</td>
                <td>{review.likeCount}</td>
                <td>{review.username}</td>
                <td>
                  {isAdmin ? (
                    <>
                      <button
                        className="button ghost"
                        type="button"
                        disabled={review.userId === user?.userId}
                        onClick={() => handleToggleLike(review)}
                      >
                        {review.likedByCurrentUser ? "Unlike" : "Like"}
                      </button>
                      <button
                        className="button ghost"
                        type="button"
                        onClick={() => setConfirmDelete(review)}
                      >
                        Delete
                      </button>
                    </>
                  ) : review.userId === user?.userId ? (
                    <>
                      <button
                        className="button ghost"
                        type="button"
                        onClick={() => openEdit(review)}
                      >
                        Edit
                      </button>
                      <button
                        className="button ghost"
                        type="button"
                        onClick={() => setConfirmDelete(review)}
                      >
                        Delete
                      </button>
                    </>
                  ) : (
                    <button
                      className="button ghost"
                      type="button"
                      onClick={() => handleToggleLike(review)}
                    >
                      {review.likedByCurrentUser ? "Unlike" : "Like"}
                    </button>
                  )}
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
      </section>

      <Modal
        open={modalOpen}
        title={editing ? "Edit review" : "Add review"}
        onClose={() => setModalOpen(false)}
      >
        <form className="form-grid" onSubmit={handleSave}>
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
            <label htmlFor="albumId">Album</label>
            <select
              id="albumId"
              name="albumId"
              required
              value={form.albumId}
              onChange={handleFormChange}
            >
              <option value="">Select album</option>
              {albums.map((album) => (
                <option key={album.id} value={album.id}>
                  {album.title}
                </option>
              ))}
            </select>
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
            <button className="button" type="submit">
              Save
            </button>
          </div>
        </form>
      </Modal>

      <ConfirmDialog
        open={Boolean(confirmDelete)}
        title="Delete review"
        message="This action is permanent. Confirm deletion?"
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onClose={() => setConfirmDelete(null)}
      />
    </main>
  );
}
