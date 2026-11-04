CREATE SCHEMA IF NOT EXISTS auth;
CREATE SCHEMA IF NOT EXISTS notes;
CREATE SCHEMA IF NOT EXISTS contact;

DO $$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'auth_app') THEN
        CREATE ROLE auth_app NOLOGIN;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'notes_app') THEN
        CREATE ROLE notes_app NOLOGIN;
    END IF;
    IF NOT EXISTS (SELECT FROM pg_roles WHERE rolname = 'contact_app') THEN
        CREATE ROLE contact_app NOLOGIN;
    END IF;
END
$$;

GRANT USAGE, CREATE ON SCHEMA auth TO auth_app;
GRANT USAGE, CREATE ON SCHEMA notes TO notes_app;
GRANT USAGE, CREATE ON SCHEMA contact TO contact_app;