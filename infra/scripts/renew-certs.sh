#!/usr/bin/env bash
set -euo pipefail

command -v mkcert >/dev/null || { printf 'Install mkcert before running this script.\n' >&2; exit 1; }
mkdir -p infra/certs
mkcert -cert-file infra/certs/localhost.pem -key-file infra/certs/localhost-key.pem localhost 127.0.0.1 ::1
printf 'Local certificate generated under infra/certs/.\n'