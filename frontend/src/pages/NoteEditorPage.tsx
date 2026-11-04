import { useEffect, useState, type FormEvent } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { request } from '../api/client';
import type { Note } from '../api/types';
import { ErrorBanner } from '../components/ErrorBanner';
import { NoteForm, type NoteDraft } from '../components/NoteForm';

const emptyDraft: NoteDraft = { title: '', body: '' };

export function NoteEditorPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [draft, setDraft] = useState(emptyDraft);
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');

  useEffect(() => {
    if (!id) {
      setDraft(emptyDraft);
      return;
    }
    request<Note>(`/api/notes/${id}`).then((note) => setDraft({ title: note.title, body: note.body }))
      .catch(() => setError('This note is unavailable.'));
  }, [id]);

  async function save(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setBusy(true);
    setError('');
    try {
      await request(id ? `/api/notes/${id}` : '/api/notes', {
        method: id ? 'PUT' : 'POST',
        body: JSON.stringify(draft),
      });
      navigate('/notes');
    } catch (issue) {
      setError(issue instanceof Error ? issue.message : 'Note could not be saved.');
    } finally {
      setBusy(false);
    }
  }

  async function remove() {
    if (!id || !window.confirm('Delete this note?')) return;
    try {
      await request<void>(`/api/notes/${id}`, { method: 'DELETE' });
      navigate('/notes');
    } catch {
      setError('Note could not be deleted.');
    }
  }

  return (
    <section className="editor-page">
      <div className="editor-toolbar"><Link to="/notes" className="back-link">← All notes</Link><div className="toolbar-actions">
        {id && <><a className="icon-button" href={`/print/${id}`} target="_blank" rel="noreferrer" aria-label="Open print view" title="Open print view">⎙</a><button className="icon-button delete-button" type="button" onClick={remove} aria-label="Delete note" title="Delete note">⌫</button></>}
      </div></div>
      <div className="note-paper"><p className="paper-date">{new Date().toLocaleDateString(undefined, { weekday: 'long', month: 'long', day: 'numeric' })}</p>
        <ErrorBanner message={error} /><NoteForm draft={draft} onChange={setDraft} onSubmit={save} saving={busy} />
      </div>
    </section>
  );
}