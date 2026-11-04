import { useState, type FormEvent } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import { useSession } from '../auth/SessionContext';
import { ErrorBanner } from '../components/ErrorBanner';

export function LoginPage() {
  const { user, loading, login } = useSession();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  if (!loading && user) return <Navigate to="/notes" replace />;

  async function submit(event: FormEvent) {
    event.preventDefault();
    setBusy(true);
    setError('');
    try {
      await login(username, password);
      navigate('/notes');
    } catch {
      setError('That username and password combination was not accepted.');
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-panel">
        <span className="mark">f.</span><p className="eyebrow">YOUR PRIVATE SPACE</p>
        <h1>Welcome back.</h1><p className="auth-copy">Pick up where your thoughts left off.</p>
        <ErrorBanner message={error} />
        <form className="stack-form" onSubmit={submit}>
          <label>Username<input autoComplete="username" value={username} onChange={(event) => setUsername(event.target.value)} required /></label>
          <label>Password<input type="password" autoComplete="current-password" value={password} onChange={(event) => setPassword(event.target.value)} required /></label>
          <button className="primary-button" disabled={busy}>{busy ? 'Signing in…' : 'Sign in'}</button>
        </form>
        <p className="auth-switch">New here? <Link to="/register">Create an account</Link></p>
      </section>
    </main>
  );
}