# mini-notes

A single-repository starter for a notes application. The repository contains a React SPA, a Spring Cloud Gateway BFF, and separate auth, notes, and contact services.

## Run locally

Prerequisites: Git, Docker Compose v2.24.4+, Java 21, Maven 3.9+, Node.js 20+, and Bash (for `infra/scripts/*.sh`). Install mkcert only if you want locally trusted HTTPS.

1. Initialize the single repository with `git init` from this directory if it is not already initialized. Do not create a nested repository.
2. Copy `.env.example` to `.env` and fill in the PostgreSQL and pgAdmin values. Copy `env/auth-service.env.example`, `env/notes-service.env.example`, and `env/contact-service.env.example` to the corresponding `.env` names; set their ports to `8081`, `8082`, and `8083`. Set a local-only `CONTACT_ADMIN_TOKEN` in `env/contact-service.env`.
3. Build and start the stack with `docker compose --env-file .env -f docker-compose.yml -f docker-compose.dev.yml up --build`.
4. Open `http://127.0.0.1:8080`. pgAdmin is available at `http://127.0.0.1:5050`.

The frontend is served by its own container. API calls go through the edge proxy and gateway. The development stack publishes backend and database ports only on loopback. Notes and contact messages are currently held in service memory; auth users and signing keys are also development-only and reset when the auth container restarts.

For locally trusted HTTPS, run `mkcert -install`, then `bash infra/scripts/renew-certs.sh`. Copy `infra/edge/conf.d/tls.conf.example` to `infra/edge/conf.d/tls.conf` and restart the stack. The dev overlay publishes HTTPS at `https://127.0.0.1:8443`.

For a frontend-only loop on Windows, run `npm.cmd install` and `npm.cmd run dev` from `frontend/`; Vite proxies `/api` to the gateway on port 8080. `env/gateway.env.example` and `env/frontend.env.example` document variables for running those apps outside Compose.

## API documentation

OpenAPI contracts are in `platform/api-contracts/`:

- `openapi-auth.yaml`
- `openapi-notes.yaml`
- `openapi-contact.yaml`

The gateway is the public API entry point at `/api`. Individual service ports are not exposed by the base Compose file.

## Deploy

Build the images with `docker compose --env-file .env build`. Configure production secrets outside the repository, provide a managed database and persistent storage, install TLS certificates under `infra/certs/`, then deploy with `docker compose --env-file .env up -d`. The checked-in defaults are for local development and are not a production security configuration.

## Backups and takedown

Run the Bash scripts as `bash infra/scripts/backup-db.sh`, `bash infra/scripts/restore-db.sh <dump>`, and `bash infra/scripts/takedown.sh`. Add `--volumes` to takedown only when intentionally deleting database data. `bash infra/scripts/renew-certs.sh` regenerates local mkcert certificates.

## Test accounts

There are no pre-seeded accounts. Register a development user with `POST /api/auth/users`, then request a token from `POST /api/auth/token`. Set `CONTACT_ADMIN_TOKEN` before using the contact admin inbox. Do not reuse development credentials outside a local environment.

## Repository phases

This repository is the initial scaffold. Add annotated Git tags `phase-1` through `phase-4` only when the corresponding phases are implemented and reviewed; no phase tags are created by the scaffold.