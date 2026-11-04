import { Navigate } from 'react-router-dom';
import { useSession } from '../auth/SessionContext';

export function ProtectedRoute({ children, admin = false }: { children: React.ReactNode; admin?: boolean }) {
  const { user, loading } = useSession();
  if (loading) return <div className="loading-state">Checking your session…</div>;
  if (!user) return <Navigate to="/login" replace />;
  if (admin && !user.roles.includes('ADMIN')) return <Navigate to="/notes" replace />;
  return children;
}