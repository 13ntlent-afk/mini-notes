#!/usr/bin/env bash
set -euo pipefail

: "${AUTH_DB_PASSWORD:?AUTH_DB_PASSWORD is required}"
: "${NOTES_DB_PASSWORD:?NOTES_DB_PASSWORD is required}"
: "${CONTACT_DB_PASSWORD:?CONTACT_DB_PASSWORD is required}"

psql --set=ON_ERROR_STOP=1 \
  --set=auth_password="$AUTH_DB_PASSWORD" \
  --set=notes_password="$NOTES_DB_PASSWORD" \
  --set=contact_password="$CONTACT_DB_PASSWORD" \
  --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<'SQL'
CREATE ROLE auth_login LOGIN PASSWORD :'auth_password';
GRANT auth_app TO auth_login;
CREATE ROLE notes_login LOGIN PASSWORD :'notes_password';
GRANT notes_app TO notes_login;
CREATE ROLE contact_login LOGIN PASSWORD :'contact_password';
GRANT contact_app TO contact_login;
SQL