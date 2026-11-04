CREATE SCHEMA IF NOT EXISTS contact;

CREATE TABLE contact.contact_messages (
    id UUID PRIMARY KEY,
    name VARCHAR(120) NOT NULL,
    email VARCHAR(254) NOT NULL,
    message_body VARCHAR(5000) NOT NULL,
    received_at TIMESTAMPTZ NOT NULL
);

CREATE INDEX contact_messages_received_idx ON contact.contact_messages (received_at DESC);