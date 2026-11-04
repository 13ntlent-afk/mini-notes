import { useState, type FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { request } from '../api/client';
import { useSession } from '../auth/SessionContext';
import { ErrorBanner } from '../components/ErrorBanner';

export function RegisterPage() {
  const { login } = useSession();
  const navigate = useNavigate();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function submit(event: FormEvent) {
    event.preventDefault();
    setBusy(true);
    setError('');
    try {
      await request('/api/auth/register', { method: 'POST', body: JSON.stringify({ username, password }) });
      await login(username, password);
      navigate('/notes');
    } catch (issue) {
      setError(issue instanceof Error ? issue.message : 'Account could not be created.');
    } finally {
      setBusy(false);
    }
  }

  return (
    <main className="auth-page">
      <section className="auth-panel">
        <span className="mark">f.</span><p className="eyebrow">A NEW PAGE</p>
        <h1>Make a little room.</h1><p className="auth-copy">Create an account for your notes.</p>
        <ErrorBanner message={error} />
        <form className="stack-form" onSubmit={submit}>
          <label>Username<input autoComplete="username" minLength={3} maxLength={80} pattern="[A-Za-z0-9._-]+" value={username} onChange={(event) => setUsername(event.target.value)} required /></label>
          <label>Password<input type="password" autoComplete="new-password" minLength={12} maxLength={128} value={password} onChange={(event) => setPassword(event.target.value)} required /><small>Use at least 12 characters.</small></label>
          <button className="primary-button" disabled={busy}>{busy ? 'Creating…' : 'Create account'}</button>
        </form>
        <p className="auth-switch">Already have an account? <Link to="/login">Sign in</Link></p>
      </section>
    </main>
  );
}