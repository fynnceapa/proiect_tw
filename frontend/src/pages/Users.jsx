import { useState } from "react";
import { request } from "../api/client.js";

export default function Users() {
  const [query, setQuery] = useState("");
  const [results, setResults] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSearch = async (event) => {
    event.preventDefault();
    setError("");
    if (!query.trim()) {
      setResults([]);
      return;
    }

    setLoading(true);
    try {
      const data = await request(`/users/search?query=${encodeURIComponent(query)}`);
      setResults(data || []);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const toggleFollow = async (profile) => {
    try {
      if (profile.followedByCurrentUser) {
        await request(`/users/follow/${profile.userId}`, { method: "DELETE" });
      } else {
        await request(`/users/follow/${profile.userId}`, { method: "POST" });
      }

      const data = await request(`/users/search?query=${encodeURIComponent(query)}`);
      setResults(data || []);
    } catch (err) {
      setError(err.message);
    }
  };

  return (
    <main className="page">
      <header className="page-header">
        <h1>Users</h1>
        <p>Search listeners and follow their review journeys.</p>
      </header>

      <section className="panel">
        <form className="toolbar" onSubmit={handleSearch}>
          <input
            type="search"
            placeholder="Search by username or email"
            value={query}
            onChange={(event) => setQuery(event.target.value)}
          />
          <button className="button secondary" type="submit">
            Search
          </button>
        </form>
        {error && <div className="alert">{error}</div>}
        {loading ? (
          <div className="panel loading">Searching...</div>
        ) : (
          <div className="user-grid">
            {results.map((profile) => (
              <div key={profile.userId} className="user-card">
                <div className="user-row">
                  <div
                    className="user-avatar"
                    style={
                      profile.avatarUrl
                        ? { backgroundImage: `url(${profile.avatarUrl})` }
                        : undefined
                    }
                  />
                  <div>
                    <strong>{profile.displayName || profile.username}</strong>
                    <small className="helper">@{profile.username}</small>
                  </div>
                </div>
                <p className="helper">{profile.bio || "No bio yet."}</p>
                <div className="user-stats">
                  <span className="tag">{profile.reviewCount} reviews</span>
                  <span className="tag">{profile.followerCount} followers</span>
                </div>
                <button
                  className={profile.followedByCurrentUser ? "button secondary" : "button"}
                  type="button"
                  onClick={() => toggleFollow(profile)}
                >
                  {profile.followedByCurrentUser ? "Unfollow" : "Follow"}
                </button>
              </div>
            ))}
            {!results.length && query.trim() && !error && (
              <p className="helper">No users found.</p>
            )}
            {!query.trim() && !error && (
              <p className="helper">Type a name or email to search.</p>
            )}
          </div>
        )}
      </section>
    </main>
  );
}
