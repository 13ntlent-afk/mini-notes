import { Link } from 'react-router-dom';
import type { Note } from '../api/types';

export function NoteCard({ note }: { note: Note }) {
  return (
    <Link className="note-card" to={`/notes/${note.id}`}>
      <span className="note-card-title">{note.title}</span>
      <span className="note-card-body">{note.body || 'An empty page, ready when you are.'}</span>
      <time dateTime={note.updatedAt}>{new Date(note.updatedAt).toLocaleDateString(undefined, { month: 'short', day: 'numeric' })}</time>
    </Link>
  );
}