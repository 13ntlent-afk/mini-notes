#!/usr/bin/env bash
set -euo pipefail

input="${1:?Usage: restore-db.sh path/to/backup.dump}"
test -f "$input"
docker compose --env-file .env exec -T db sh -c 'pg_restore -U "$POSTGRES_USER" -d "$POSTGRES_DB" --clean --if-exists' < "$input"