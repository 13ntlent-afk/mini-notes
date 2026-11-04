import { useEffect, useState } from 'react';
import { request } from '../api/client';
import type { ContactMessage } from '../api/types';
import { ErrorBanner } from '../components/ErrorBanner';
import { ProtectedRoute } from '../components/ProtectedRoute';

function Inbox() {
  const [messages, setMessages] = useState<ContactMessage[]>([]);
  const [error, setError] = useState('');

  useEffect(() => {
    request<ContactMessage[]>('/api/admin/messages').then(setMessages)
      .catch(() => setError('The inbox could not be loaded.'));
  }, []);

  return <section className="inbox-page"><p className="eyebrow">ADMIN</p><h1>Contact inbox.</h1><ErrorBanner message={error} />
    {messages.length === 0 && !error ? <p className="empty-message">The inbox is clear.</p> : <div className="message-list">
      {messages.map((message) => <article className="message-row" key={message.id}>
        <header><div><h2>{message.name}</h2><a href={`mailto:${message.email}`}>{message.email}</a></div><time>{new Date(message.receivedAt).toLocaleString()}</time></header>
        <p>{message.message}</p>
      </article>)}
    </div>}
  </section>;
}

export function AdminInboxPage() {
  return <ProtectedRoute admin><Inbox /></ProtectedRoute>;
}