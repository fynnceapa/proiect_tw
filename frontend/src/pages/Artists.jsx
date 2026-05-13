import { useEffect, useMemo, useState } from "react";
import { request } from "../api/client.js";
import Modal from "../components/Modal.jsx";
import ConfirmDialog from "../components/ConfirmDialog.jsx";
import Pagination from "../components/Pagination.jsx";
import { useAuth } from "../contexts/AuthContext.jsx";

const emptyForm = {
  name: "",
  bio: "",
  imageUrl: ""
};

export default function Artists() {
  const [artists, setArtists] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const { isAdmin } = useAuth();

  const pageSize = 8;

  const pagedArtists = useMemo(() => {
    const start = (page - 1) * pageSize;
    return artists.slice(start, start + pageSize);
  }, [artists, page]);

  const totalPages = Math.max(1, Math.ceil(artists.length / pageSize));

  const loadArtists = async (query = "") => {
    setLoading(true);
    setError("");
    try {
      const data = query
        ? await request(`/artists/search?name=${encodeURIComponent(query)}`)
        : await request("/artists");
      setArtists(data || []);
      setPage(1);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadArtists();
  }, []);

  const handleSearch = (event) => {
    event.preventDefault();
    loadArtists(search);
  };

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (artist) => {
    setEditing(artist);
    setForm({
      name: artist.name || "",
      bio: artist.bio || "",
      imageUrl: artist.imageUrl || ""
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
      name: form.name,
      bio: form.bio || null,
      imageUrl: form.imageUrl || null
    };

    try {
      if (editing) {
        await request(`/artists/${editing.id}`, {
          method: "PUT",
          body: JSON.stringify(payload)
        });
      } else {
        await request("/artists", {
          method: "POST",
          body: JSON.stringify(payload)
        });
      }
      setModalOpen(false);
      await loadArtists(search);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!confirmDelete) {
      return;
    }

    try {
      await request(`/artists/${confirmDelete.id}`, { method: "DELETE" });
      setConfirmDelete(null);
      await loadArtists(search);
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <main className="page">
      <header className="page-header">
        <h1>Artists</h1>
        <p>Discover who shapes the albums you love.</p>
      </header>

      <section className="panel">
        <form className="toolbar" onSubmit={handleSearch}>
          <input
            type="search"
            placeholder="Search artist by name"
            value={search}
            onChange={(event) => setSearch(event.target.value)}
          />
          <div className="form-actions">
            <button className="button secondary" type="submit">
              Search
            </button>
            {isAdmin && (
              <button className="button" type="button" onClick={openCreate}>
                Add artist
              </button>
            )}
          </div>
        </form>
        {error && <div className="alert">{error}</div>}
        {loading ? (
          <div className="panel loading">Loading artists...</div>
        ) : (
          <>
            <table className="table">
              <thead>
                <tr>
                  <th>Name</th>
                  <th>Bio</th>
                  <th>Image URL</th>
                  <th>Created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {pagedArtists.map((artist) => (
                  <tr key={artist.id}>
                    <td>{artist.name}</td>
                    <td>{artist.bio || "-"}</td>
                    <td>{artist.imageUrl || "-"}</td>
                    <td>{artist.createdAt?.slice(0, 10) || "-"}</td>
                    <td>
                      {isAdmin ? (
                        <>
                          <button
                            className="button ghost"
                            type="button"
                            onClick={() => openEdit(artist)}
                          >
                            Edit
                          </button>
                          <button
                            className="button ghost"
                            type="button"
                            onClick={() => setConfirmDelete(artist)}
                          >
                            Delete
                          </button>
                        </>
                      ) : (
                        "-"
                      )}
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
            <Pagination page={page} totalPages={totalPages} onPageChange={setPage} />
          </>
        )}
      </section>

      <Modal
        open={modalOpen}
        title={editing ? "Edit artist" : "Add artist"}
        onClose={() => setModalOpen(false)}
      >
        <form className="form-grid" onSubmit={handleSave}>
          <div className="form-row">
            <label htmlFor="name">Name</label>
            <input
              id="name"
              name="name"
              required
              value={form.name}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="bio">Bio</label>
            <textarea
              id="bio"
              name="bio"
              value={form.bio}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="imageUrl">Image URL</label>
            <input
              id="imageUrl"
              name="imageUrl"
              value={form.imageUrl}
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
        title="Delete artist"
        message="This action is permanent. Confirm deletion?"
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onClose={() => setConfirmDelete(null)}
      />
    </main>
  );
}
