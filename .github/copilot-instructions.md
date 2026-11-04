# Repository Instructions

- Keep this as one Git repository rooted at `mini-notes/`.
- Keep service-specific code in its owning service; shared dependency versions belong in `platform/bom` and public API contracts in `platform/api-contracts`.
- Never commit secrets, local environment files, certificates, build outputs, or generated dependencies.
- Preserve path filters in CI when adding or moving project files.
- Validate frontend changes with the frontend build and Java changes with the relevant Maven module plus `-am`.
- Treat Compose defaults and in-memory development data as local-only, never production-ready.