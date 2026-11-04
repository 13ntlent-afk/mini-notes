import { useState, type FormEvent } from 'react';
import { request } from '../api/client';
import { ErrorBanner } from '../components/ErrorBanner';

export function ContactPage() {
  const [sent, setSent] = useState(false);
  const [error, setError] = useState('');
  const [busy, setBusy] = useState(false);

  async function submit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    const formElement = event.currentTarget;
    setBusy(true);
    setError('');
    const form = new FormData(formElement);
    try {
      await request('/api/contact', { method: 'POST', body: JSON.stringify({
        name: form.get('name'), email: form.get('email'), message: form.get('message'),
      }) });
      setSent(true);
      formElement.reset();
    } catch (issue) {
      setError(issue instanceof Error ? issue.message : 'Message could not be sent.');
    } finally {
      setBusy(false);
    }
  }

  return (
    <section className="form-page">
      <p className="eyebrow">SAY HELLO</p><h1>Send a note.</h1>
      {sent ? <p className="success-message" role="status">Your message is on its way. Thank you.</p> : <>
        <ErrorBanner message={error} />
        <form className="stack-form" onSubmit={submit}>
          <label>Name<input name="name" autoComplete="name" maxLength={120} required /></label>
          <label>Email<input name="email" type="email" autoComplete="email" maxLength={254} required /></label>
          <label>Message<textarea name="message" rows={7} maxLength={5000} required /></label>
          <button className="primary-button" disabled={busy}>{busy ? 'Sending…' : 'Send message'}</button>
        </form>
      </>}
    </section>
  );
}