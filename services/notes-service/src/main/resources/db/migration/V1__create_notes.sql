CREATE SCHEMA IF NOT EXISTS notes;

CREATE TABLE notes.notes (
    id UUID PRIMARY KEY,
    user_id UUID NOT NULL,
    title VARCHAR(160) NOT NULL,
    body VARCHAR(20000) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX notes_user_updated_idx ON notes.notes (user_id, updated_at DESC);