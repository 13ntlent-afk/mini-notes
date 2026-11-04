# Project Writeup

## Scope

Mini Notes is organized as a single Git monorepo with a separate React frontend, a Spring Cloud Gateway BFF, and independent auth, notes, and contact services. Shared Maven dependency versions and public API contracts live under `platform/`.

## Implementation status

This commit is a runnable starter scaffold, not a production release. The SPA and notes CRUD/print endpoints are wired through the edge and gateway. Auth demonstrates development user registration, password hashing, RS256 token issuance, and JWKS publication. Contact submission and a token-gated development inbox are present. Service data remains in memory; PostgreSQL persistence, token validation at the gateway, and durable key management remain follow-on work.

## Phase evidence

Add screenshots, test output, deployment notes, and relevant decisions under `docs/evidence/phase-N/` as each phase is completed. Create a Git tag only after review confirms the phase acceptance criteria.