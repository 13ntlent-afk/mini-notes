import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { request } from '../api/client';
import type { Note } from '../api/types';
import { ErrorBanner } from '../components/ErrorBanner';
import { NoteCard } from '../components/NoteCard';

export function NotesPage() {
  const [notes, setNotes] = useState<Note[]>([]);
  const [filter, setFilter] = useState('');
  const [error, setError] = useState('');

  useEffect(() => {
    request<Note[]>('/api/notes').then(setNotes).catch(() => setError('Your notes could not be loaded.'));
  }, []);

  const visibleNotes = notes.filter((note) => `${note.title} ${note.body}`.toLowerCase().includes(filter.toLowerCase()));

  return (
    <section className="notes-page">
      <header className="page-heading">
        <div><p className="eyebrow">A FRESH PAGE</p><h1>Your notes<span className="period">.</span></h1></div>
        <Link className="primary-button compact" to="/notes/new"><span aria-hidden="true">+</span> New note</Link>
      </header>
      <ErrorBanner message={error} />
      <label className="search-box"><span aria-hidden="true">⌕</span><input value={filter} onChange={(event) => setFilter(event.target.value)} placeholder="Find a note" aria-label="Find a note" /></label>
      <p className="list-count">{visibleNotes.length} {visibleNotes.length === 1 ? 'note' : 'notes'}</p>
      {visibleNotes.length ? <div className="notes-grid">{visibleNotes.map((note) => <NoteCard key={note.id} note={note} />)}</div> :
        <div className="empty-list"><span className="empty-glyph" aria-hidden="true">✳</span><p>{filter ? 'No notes match that search.' : 'A little room for a big thought.'}</p>{!filter && <Link to="/notes/new">Write your first note ↗</Link>}</div>}
    </section>
  );
}