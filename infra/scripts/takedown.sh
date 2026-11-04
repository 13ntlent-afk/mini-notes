#!/usr/bin/env bash
set -euo pipefail

args=()
if [[ "${1:-}" == "--volumes" ]]; then
  args+=(--volumes)
elif [[ $# -gt 0 ]]; then
  printf 'Usage: takedown.sh [--volumes]\n' >&2
  exit 2
fi
docker compose --env-file .env -f docker-compose.yml -f docker-compose.dev.yml down "${args[@]}"