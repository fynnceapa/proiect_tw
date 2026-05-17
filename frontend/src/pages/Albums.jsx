import { useEffect, useMemo, useState } from "react";
import { Link } from "react-router-dom";
import { request } from "../api/client.js";
import Modal from "../components/Modal.jsx";
import ConfirmDialog from "../components/ConfirmDialog.jsx";
import Pagination from "../components/Pagination.jsx";
import { useAuth } from "../contexts/AuthContext.jsx";

const emptyForm = {
  title: "",
  releaseYear: "",
  coverImageUrl: "",
  artistId: "",
  genreIds: []
};

export default function Albums() {
  const [albums, setAlbums] = useState([]);
  const [artists, setArtists] = useState([]);
  const [genres, setGenres] = useState([]);
  const [search, setSearch] = useState("");
  const [page, setPage] = useState(1);
  const [sortBy, setSortBy] = useState("title-asc");
  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);
  const [modalOpen, setModalOpen] = useState(false);
  const [editing, setEditing] = useState(null);
  const [form, setForm] = useState(emptyForm);
  const [confirmDelete, setConfirmDelete] = useState(null);
  const { isAdmin } = useAuth();

  const pageSize = 8;

  const sortedAlbums = useMemo(() => {
    const sorted = [...albums];

    const getTitle = (value) => (value?.title || "").toLowerCase();
    const getYear = (value, fallback) => {
      const year = Number(value?.releaseYear);
      return Number.isFinite(year) ? year : fallback;
    };
    const getRating = (value, fallback) => {
      const rating = Number(value?.averageRating);
      return Number.isFinite(rating) ? rating : fallback;
    };

    sorted.sort((a, b) => {
      switch (sortBy) {
        case "title-desc":
          return getTitle(b).localeCompare(getTitle(a));
        case "year-desc":
          return getYear(b, -1) - getYear(a, -1);
        case "year-asc":
          return getYear(a, Number.MAX_SAFE_INTEGER) - getYear(b, Number.MAX_SAFE_INTEGER);
        case "rating-desc":
          return getRating(b, -1) - getRating(a, -1);
        case "rating-asc":
          return getRating(a, Number.MAX_SAFE_INTEGER) - getRating(b, Number.MAX_SAFE_INTEGER);
        case "title-asc":
        default:
          return getTitle(a).localeCompare(getTitle(b));
      }
    });

    return sorted;
  }, [albums, sortBy]);

  const pagedAlbums = useMemo(() => {
    const start = (page - 1) * pageSize;
    return sortedAlbums.slice(start, start + pageSize);
  }, [sortedAlbums, page]);

  const totalPages = Math.max(1, Math.ceil(albums.length / pageSize));

  const loadMeta = async () => {
    const [artistData, genreData] = await Promise.all([
      request("/artists"),
      request("/genres")
    ]);
    setArtists(artistData || []);
    setGenres(genreData || []);
  };

  const loadAlbums = async (query = "") => {
    setLoading(true);
    setError("");
    try {
      const data = query
        ? await request(`/albums/search?title=${encodeURIComponent(query)}`)
        : await request("/albums");
      setAlbums(data || []);
      setPage(1);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadMeta();
    loadAlbums();
  }, []);

  const openCreate = () => {
    setEditing(null);
    setForm(emptyForm);
    setModalOpen(true);
  };

  const openEdit = (album) => {
    setEditing(album);
    setForm({
      title: album.title || "",
      releaseYear: album.releaseYear || "",
      coverImageUrl: album.coverImageUrl || "",
      artistId: album.artist?.id || "",
      genreIds: album.genres?.map((genre) => genre.id) || []
    });
    setModalOpen(true);
  };

  const handleFormChange = (event) => {
    setForm((prev) => ({ ...prev, [event.target.name]: event.target.value }));
  };

  const toggleGenre = (genreId) => {
    setForm((prev) => {
      const exists = prev.genreIds.includes(genreId);
      return {
        ...prev,
        genreIds: exists
          ? prev.genreIds.filter((id) => id !== genreId)
          : [...prev.genreIds, genreId]
      };
    });
  };

  const handleSave = async (event) => {
    event.preventDefault();
    setError("");
    const payload = {
      title: form.title,
      releaseYear: form.releaseYear ? Number(form.releaseYear) : null,
      coverImageUrl: form.coverImageUrl || null,
      artistId: form.artistId,
      genreIds: form.genreIds
    };

    try {
      if (editing) {
        await request(`/albums/${editing.id}`, {
          method: "PUT",
          body: JSON.stringify(payload)
        });
      } else {
        await request("/albums", {
          method: "POST",
          body: JSON.stringify(payload)
        });
      }
      setModalOpen(false);
      await loadAlbums(search);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDelete = async () => {
    if (!confirmDelete) {
      return;
    }

    try {
      await request(`/albums/${confirmDelete.id}`, { method: "DELETE" });
      setConfirmDelete(null);
      await loadAlbums(search);
    } catch (err) {
      setError(err.message);
    }
  };

  const handleSearch = (event) => {
    event.preventDefault();
    loadAlbums(search);
  };

  const handleSortChange = (event) => {
    setSortBy(event.target.value);
    setPage(1);
  };

  return (
    <main className="page">
      <header className="page-header">
        <h1>Albums</h1>
        <p>Browse the catalog.</p>
      </header>

      <section className="panel catalog-panel">
        <form className="toolbar catalog-toolbar" onSubmit={handleSearch}>
          <div className="catalog-controls">
            <input
              type="search"
              placeholder="Search by title"
              value={search}
              onChange={(event) => setSearch(event.target.value)}
            />
            <div className="catalog-sort">
              <select
                aria-label="Sort albums"
                value={sortBy}
                onChange={handleSortChange}
              >
                <option value="title-asc">Title (A-Z)</option>
                <option value="title-desc">Title (Z-A)</option>
                <option value="year-desc">Year (newest)</option>
                <option value="year-asc">Year (oldest)</option>
                <option value="rating-desc">Rating (high)</option>
                <option value="rating-asc">Rating (low)</option>
              </select>
            </div>
          </div>
          <div className="form-actions catalog-actions">
            <button className="button secondary" type="submit">
              Search
            </button>
            {isAdmin && (
              <button
                className="button"
                type="button"
                onClick={openCreate}
              >
                Add album
              </button>
            )}
          </div>
        </form>
        {error && <div className="alert">{error}</div>}
        {loading ? (
          <div className="panel loading">Loading albums...</div>
        ) : (
          <>
            <div className="album-grid catalog-grid">
              {pagedAlbums.map((album) => (
                <div key={album.id} className="album-card">
                  <Link
                    to={`/albums/${album.id}`}
                    className="album-cover-link"
                    style={
                      album.coverImageUrl
                        ? { backgroundImage: `url(${album.coverImageUrl})` }
                        : undefined
                    }
                    aria-label={`View ${album.title}`}
                  >
                    {!album.coverImageUrl && (
                      <span className="album-cover-fallback">No cover</span>
                    )}
                  </Link>
                  <div className="album-info">
                    <Link to={`/albums/${album.id}`} className="album-title">
                      {album.title}
                    </Link>
                    <span className="helper">{album.artist?.name || "Unknown"}</span>
                    <span className="helper">
                      {album.releaseYear || "Year unknown"}
                    </span>
                    <span className="tag">
                      Rating {album.averageRating?.toFixed?.(1) || "-"}
                    </span>
                  </div>
                  {isAdmin && (
                    <div className="album-actions">
                      <button
                        className="button ghost"
                        type="button"
                        onClick={() => openEdit(album)}
                      >
                        Edit
                      </button>
                      <button
                        className="button ghost"
                        type="button"
                        onClick={() => setConfirmDelete(album)}
                      >
                        Delete
                      </button>
                    </div>
                  )}
                </div>
              ))}
            </div>
            <div className="catalog-pagination">
              <Pagination
                page={page}
                totalPages={totalPages}
                onPageChange={setPage}
              />
            </div>
          </>
        )}
      </section>

      <Modal
        open={modalOpen}
        title={editing ? "Edit album" : "Add album"}
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
            <label htmlFor="releaseYear">Release year</label>
            <input
              id="releaseYear"
              name="releaseYear"
              type="number"
              value={form.releaseYear}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="coverImageUrl">Cover image URL</label>
            <input
              id="coverImageUrl"
              name="coverImageUrl"
              value={form.coverImageUrl}
              onChange={handleFormChange}
            />
          </div>
          <div className="form-row">
            <label htmlFor="artistId">Artist</label>
            <select
              id="artistId"
              name="artistId"
              required
              value={form.artistId}
              onChange={handleFormChange}
            >
              <option value="">Select artist</option>
              {artists.map((artist) => (
                <option key={artist.id} value={artist.id}>
                  {artist.name}
                </option>
              ))}
            </select>
          </div>
          <div className="form-row">
            <label>Genres</label>
            <div className="panel-grid">
              {genres.map((genre) => (
                <label key={genre.id} className="helper">
                  <input
                    type="checkbox"
                    checked={form.genreIds.includes(genre.id)}
                    onChange={() => toggleGenre(genre.id)}
                  />
                  {" "}{genre.name}
                </label>
              ))}
            </div>
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
        title="Delete album"
        message="This action is permanent. Confirm deletion?"
        confirmLabel="Delete"
        onConfirm={handleDelete}
        onClose={() => setConfirmDelete(null)}
      />
    </main>
  );
}
