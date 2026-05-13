import { createContext, useContext, useEffect, useMemo, useState } from "react";
import { clearToken, getToken, request, setToken } from "../api/client.js";

const AuthContext = createContext(null);

export function AuthProvider({ children }) {
  const [token, setTokenState] = useState(() => getToken());
  const [user, setUser] = useState(null);
  const [loading, setLoading] = useState(false);
  const [roles, setRoles] = useState([]);

  const parseTokenRoles = (jwt) => {
    if (!jwt) {
      return [];
    }

    const parts = jwt.split(".");
    if (parts.length < 2) {
      return [];
    }

    const base64 = parts[1].replace(/-/g, "+").replace(/_/g, "/");
    const padded = base64 + "===".slice((base64.length + 3) % 4);

    try {
      const payload = JSON.parse(atob(padded));
      return Array.isArray(payload?.roles) ? payload.roles : [];
    } catch {
      return [];
    }
  };

  const fetchProfile = async () => {
    if (!token) {
      setUser(null);
      return;
    }

    setLoading(true);
    try {
      const profile = await request("/users/me");
      setUser(profile);
    } catch (err) {
      setUser(null);
      if (err?.status === 401 || err?.status === 403) {
        clearToken();
        setTokenState(null);
      }
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    setRoles(parseTokenRoles(token));
    fetchProfile();
  }, [token]);

  const login = async (email, password) => {
    const data = await request("/auth/login", {
      method: "POST",
      body: JSON.stringify({ email, password })
    });
    setToken(data.access_token);
    setTokenState(data.access_token);
    await fetchProfile();
    return data;
  };

  const register = async ({ email, username, password, displayName, bio, avatarUrl }) => {
    await request("/auth/register", {
      method: "POST",
      body: JSON.stringify({ email, username, password })
    });

    await login(email, password);

    if (displayName || bio || avatarUrl) {
      await request("/users/me", {
        method: "PUT",
        body: JSON.stringify({
          displayName: displayName || "",
          bio: bio || "",
          avatarUrl: avatarUrl || ""
        })
      });
      await fetchProfile();
    }
  };

  const logout = () => {
    clearToken();
    setTokenState(null);
    setUser(null);
  };

  const isAdmin = roles.includes("ADMIN");

  const value = useMemo(
    () => ({
      token,
      user,
      roles,
      isAdmin,
      loading,
      login,
      register,
      logout,
      refresh: fetchProfile
    }),
    [token, user, roles, isAdmin, loading]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  return useContext(AuthContext);
}
