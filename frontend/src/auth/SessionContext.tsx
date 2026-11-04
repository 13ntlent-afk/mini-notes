import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';
import { ApiError, request } from '../api/client';

export interface SessionUser {
  id: string;
  username: string;
  roles: string[];
}

interface SessionContextValue {
  user: SessionUser | null;
  loading: boolean;
  login: (username: string, password: string) => Promise<void>;
  logout: () => Promise<void>;
  refresh: () => Promise<void>;
}

const SessionContext = createContext<SessionContextValue | null>(null);

export function SessionProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<SessionUser | null>(null);
  const [loading, setLoading] = useState(true);

  async function refresh() {
    try {
      setUser(await request<SessionUser>('/bff/me'));
    } catch (error) {
      if (!(error instanceof ApiError && error.status === 401)) throw error;
      setUser(null);
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    void refresh().catch(() => setUser(null));
  }, []);

  async function login(username: string, password: string) {
    setUser(await request<SessionUser>('/bff/login', {
      method: 'POST',
      body: JSON.stringify({ username, password }),
    }));
  }

  async function logout() {
    await request<void>('/bff/logout', { method: 'POST' });
    setUser(null);
  }

  return (
    <SessionContext.Provider value={{ user, loading, login, logout, refresh }}>
      {children}
    </SessionContext.Provider>
  );
}

export function useSession() {
  const context = useContext(SessionContext);
  if (!context) throw new Error('useSession must be used inside SessionProvider');
  return context;
}