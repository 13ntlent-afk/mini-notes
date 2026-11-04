#!/usr/bin/env bash
set -euo pipefail

output="${1:-backups/mini-notes-$(date +%Y%m%d-%H%M%S).dump}"
mkdir -p "$(dirname "$output")"
docker compose --env-file .env exec -T db sh -c 'pg_dump -U "$POSTGRES_USER" -d "$POSTGRES_DB" -Fc' > "$output"
printf 'Database backup written to %s\n' "$output"