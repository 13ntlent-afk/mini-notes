# Local Testing Guide

How to run the mini-notes backend and frontend locally against your own PostgreSQL `mininotes` database (single `public` schema), and test the APIs with Insomnia.

## Prerequisites

| Tool | Version |
|---|---|
| JDK | 21 (`JAVA_HOME` set) |
| Node.js | 22+ |
| PostgreSQL | Running locally with a `mininotes` database |
| Insomnia | Any recent version |

Check:
```powershell
java -version
node -v
psql --version
```

## Ports

| Service | Port |
|---|---|
| gateway | 8080 |
| auth-service | 8081 |
| notes-service | 8082 |
| contact-service | 8083 |
| frontend (Vite) | 5173 |

---

## 1. Database

The schema is owned by the hand-written DDL scripts (Flyway is disabled and Hibernate does not create or validate tables).

Run the scripts **in this order** (users first; it creates the trigger function and the `users` table the others reference):

```powershell
psql -U postgres -d mininotes -f ddl_user_table.sql
psql -U postgres -d mininotes -f ddl_notes_table.sql
psql -U postgres -d mininotes -f ddl_user_contact_messages.sql
```

### Known DDL caveats
- `contact_messages.user_id` is `NOT NULL` but the FK uses `ON DELETE SET NULL`. Deleting a user who has messages will fail. Fix by dropping `NOT NULL` or using `ON DELETE CASCADE`.
- `notes.title` is `UNIQUE` across **all** users. The API returns `409 Conflict` on duplicates. Consider `UNIQUE (user_id, title)` if titles should be unique per user only.
- `users.enabled` has no default. The app treats `NULL` as enabled; only `FALSE` blocks login.

---

## 2. Build

From the repository root:

```powershell
.\mvnw.cmd -DskipTests install
```
macOS/Linux: `./mvnw -DskipTests install`

---

## 3. Run the backend

Open **one terminal per service** and start them in this order: auth → notes → contact → gateway.

### Common variables (auth, notes, contact terminals)

PowerShell:
```powershell
$env:SPRING_PROFILES_ACTIVE="dev"
$env:SPRING_DATASOURCE_PASSWORD="<your-pg-password>"
$env:JWT_ISSUER="http://localhost:8081"
$env:JWT_AUDIENCE="mini-notes"
```

macOS/Linux:
```bash
export SPRING_PROFILES_ACTIVE=dev
export SPRING_DATASOURCE_PASSWORD='<your-pg-password>'
export JWT_ISSUER=http://localhost:8081
export JWT_AUDIENCE=mini-notes
```

Defaults already point to `jdbc:postgresql://localhost:5432/mininotes`, user `postgres`, schema `public`. Override if needed:
```powershell
$env:SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:5432/mininotes"
$env:SPRING_DATASOURCE_USERNAME="postgres"
$env:DB_SCHEMA="public"
```

### Terminal 1 — auth-service (8081)
The `dev` profile generates an in-memory signing key, so no key files are needed.
```powershell
$env:ADMIN_BOOTSTRAP_USERNAME="admin"
$env:ADMIN_BOOTSTRAP_EMAIL="admin@example.com"
$env:ADMIN_BOOTSTRAP_PASSWORD="<at-least-12-chars>"
.\mvnw.cmd -f services\auth-service\pom.xml spring-boot:run
```

### Terminal 2 — notes-service (8082)
```powershell
$env:JWKS_URI="http://localhost:8081/.well-known/jwks.json"
.\mvnw.cmd -f services\notes-service\pom.xml spring-boot:run
```

### Terminal 3 — contact-service (8083)
```powershell
$env:JWKS_URI="http://localhost:8081/.well-known/jwks.json"
.\mvnw.cmd -f services\contact-service\pom.xml spring-boot:run
```

### Terminal 4 — gateway (8080)
Common variables are not needed here.
```powershell
$env:AUTH_SERVICE_URL="http://localhost:8081"
$env:NOTES_SERVICE_URL="http://localhost:8082"
$env:CONTACT_SERVICE_URL="http://localhost:8083"
$env:SESSION_COOKIE_SECURE="false"
.\mvnw.cmd -f services\gateway\pom.xml spring-boot:run
```

### Health check
`GET http://localhost:808X/actuator/health` → `{"status":"UP"}`

> If auth-service restarts, the dev signing key changes — request a new token.

---

## 4. Test with Insomnia

### A. Direct to services (Bearer token)

| # | Request | Body / Auth |
|---|---|---|
| 1 | `POST http://localhost:8081/auth/register` | `{"username":"alice","email":"alice@example.com","password":"alicePassword123"}` |
| 2 | `POST http://localhost:8081/auth/token` | `{"username":"alice","password":"alicePassword123"}` → copy `accessToken` |
| 3 | `GET http://localhost:8082/notes` | Auth → Bearer → paste token |
| 4 | `POST http://localhost:8082/notes` | Bearer + `{"title":"First","body":"Hello"}` |
| 5 | `GET / PUT / DELETE http://localhost:8082/notes/{id}` | Bearer (PUT body same as above) |
| 6 | `POST http://localhost:8083/contact` | Bearer + `{"name":"Alice","email":"alice@example.com","subject":"Hi","message":"Hello"}` |
| 7 | `GET http://localhost:8083/admin/messages` | Bearer token of the **admin** user (normal users get 403) |

Expected errors worth testing:
- Duplicate note title → `409`
- Duplicate username or email on register → `409`
- No/expired token → `401`
- Note owned by another user → `404`

Tokens expire after 15 minutes.

### B. Through the gateway (BFF, cookie + CSRF)

Insomnia stores cookies automatically.

1. `GET http://localhost:8080/bff/csrf` → copy `token`.
2. `POST http://localhost:8080/bff/login`
   - Header: `X-XSRF-TOKEN: <token>`
   - Body: `{"username":"alice","password":"alicePassword123"}`
3. `GET http://localhost:8080/bff/me` → current user.
4. `GET http://localhost:8080/api/notes` (cookie only).
5. For `POST` / `PUT` / `DELETE` on `/api/notes...`, `/api/contact`, `/bff/logout`, add the `X-XSRF-TOKEN` header. If you get `403`, call `/bff/csrf` again.
6. Admin inbox: log in as admin, then `GET http://localhost:8080/api/admin/messages`.

---

## 5. Run the frontend

```powershell
cd frontend
npm install
npm run dev
```

Open **http://localhost:5173/app/**.

Vite proxies `/api`, `/bff`, and `/print` to `http://127.0.0.1:8080` (the gateway). No `.env` file needed. To point elsewhere:
```powershell
$env:VITE_GATEWAY_TARGET="http://127.0.0.1:8080"
```

### End-to-end check
1. Register (username, email, password ≥ 12 chars).
2. Create, edit, and delete a note.
3. Send a message from the Contact page (requires login).
4. Log in as `admin` and open the Admin Inbox.

---

## Troubleshooting

| Symptom | Likely cause |
|---|---|
| `JAVA_HOME is not defined correctly` | Set `JAVA_HOME` to the JDK 21 folder |
| `relation "users" does not exist` | DDL not run, or wrong database/schema |
| `column "role" is of type user_role but expression is of type character varying` | Running an old build — rebuild with step 2 |
| `401` on notes/contact | Token expired, or `JWT_ISSUER` / `JWT_AUDIENCE` differ between services |
| `401` right after auth-service restart | Dev signing key changed — get a new token |
| `403` through gateway on POST/PUT/DELETE | Missing or stale `X-XSRF-TOKEN` header |
| `503` / fallback from gateway | Downstream service not running or wrong `*_SERVICE_URL` |
| Frontend shows blank page | Open `/app/` (Vite base path), not `/` |
