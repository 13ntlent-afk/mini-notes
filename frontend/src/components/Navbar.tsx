import { NavLink, useNavigate } from 'react-router-dom';
import { useState } from 'react';
import { useSession } from '../auth/SessionContext';
import { ErrorBanner } from './ErrorBanner';

export function Navbar() {
  const { user, logout } = useSession();
  const navigate = useNavigate();
  const [error, setError] = useState('');

  async function signOut() {
    try {
      await logout();
      navigate('/login');
    } catch {
      setError('Could not sign out. Please try again.');
    }
  }

  return (
    <>
      <header className="topbar">
        <NavLink className="wordmark" to="/notes"><span className="mark">f.</span><span>fieldnotes</span></NavLink>
        <nav className="top-nav" aria-label="Primary navigation">
          <NavLink to="/notes">Notes</NavLink>
          <NavLink to="/contact">Contact</NavLink>
          {user?.roles.includes('ADMIN') && <NavLink to="/admin">Inbox</NavLink>}
        </nav>
        <div className="user-tools"><span>{user?.username}</span><button className="text-button" onClick={signOut} type="button">Sign out</button></div>
      </header>
      <ErrorBanner message={error} />
    </>
  );
}