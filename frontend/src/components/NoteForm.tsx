import type { FormEvent } from 'react';

export interface NoteDraft {
  title: string;
  body: string;
}

export function NoteForm({ draft, onChange, onSubmit, saving }: {
  draft: NoteDraft;
  onChange: (draft: NoteDraft) => void;
  onSubmit: (event: FormEvent<HTMLFormElement>) => void;
  saving: boolean;
}) {
  return (
    <form className="note-form" onSubmit={onSubmit}>
      <label className="field-label" htmlFor="note-title">Title</label>
      <input id="note-title" className="title-input" value={draft.title} maxLength={200} required
        onChange={(event) => onChange({ ...draft, title: event.target.value })} placeholder="A title for this thought" />
      <label className="field-label" htmlFor="note-body">Note</label>
      <textarea id="note-body" className="body-input" value={draft.body} maxLength={10000}
        onChange={(event) => onChange({ ...draft, body: event.target.value })} placeholder="Start wherever you are..." />
      <div className="form-footer"><span>{draft.body.length} characters</span><button className="save-button" disabled={saving}>{saving ? 'Saving…' : 'Save note'}</button></div>
    </form>
  );
}