# Phase Workflow - Quick Reference

**TL;DR**: Use this for quick phase-based Git workflow with tags.

---

## The Pattern

```
Commit → Commit → Commit → TAG PHASE ✓
  ↓        ↓        ↓          ↓
Phase 1    Phase 1   Phase 1   phase-1 tag
 work1     work2     work3     (created)
```

---

## Workflow Commands

### 1. Start a Phase

```bash
# Start phase branch (optional but recommended)
git checkout -b phase-1-setup

# Or if continuing on main
git checkout main
```

### 2. During the Phase - Make Commits

```bash
# After completing a task, commit with this format:
git add <files>
git commit -m "feat: [Phase-1] Your feature title

Detailed description of changes:
- What you added/fixed
- Why you made this change
- Key implementation details"
```

**Common commit types:**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `chore:` - Maintenance (deps, config)
- `test:` - Testing
- `ci:` - CI/CD

**Example commits:**

```bash
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for Node.js services
- Add docker-compose.yml with PostgreSQL and Redis
- Configure environment variables for local dev"

git commit -m "feat: [Phase-1] Initialize project structure

- Create src/ directory with service subdirectories
- Add package.json with npm scripts
- Configure ESLint and Prettier"

git commit -m "chore: [Phase-1] Set up GitHub Actions CI pipeline

- Add lint workflow
- Add build workflow
- Configure automated testing on PR"
```

### 3. When Phase Checkpoint Passes - Tag It

Verify all checkpoint tasks are complete, then:

```bash
# Create an annotated tag with a message
git tag -a phase-1 -m "Phase 1 Complete: Project setup and infrastructure

Completed:
✓ Docker containerization
✓ Project structure and linting
✓ CI/CD pipeline foundation
✓ Development environment documentation"

# View the tag
git show phase-1

# List all tags
git tag -l
```

### 4. Continue to Next Phase

```bash
# Stay on main branch
git checkout main

# Or create a new branch for Phase 2
git checkout -b phase-2-database

# ... make commits for Phase 2 ...

# When complete, tag it
git tag -a phase-2 -m "Phase 2 Complete: Database schema and ORM"
```

---

## Viewing Your Progress

```bash
# See commits in a nice graph
git log --oneline --graph --all

# See all tags
git tag -l

# See commits since last tag
git log phase-1..HEAD --oneline

# See what changed in a phase
git show phase-1    # see tag info + first commit
git diff phase-0 phase-1 --stat   # see file changes

# Checkout a previous phase (read-only viewing)
git checkout phase-1

# Return to main
git checkout main
```

---

## Real-World Example

```bash
# DAY 1: Phase 1 - Project Setup
cd C:\Users\lalainne.anne.j.abel\Documents\mini-notes
git checkout -b phase-1-setup

# Commit 1: Docker setup
git add Dockerfile docker-compose.yml .dockerignore
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for Node.js microservices
- Add docker-compose.yml with PostgreSQL and Redis
- Set up development environment file (.env.example)"

# Commit 2: Project structure
git add src/ package.json
git commit -m "feat: [Phase-1] Initialize project structure

- Create src/ with services, gateway, shared subdirectories
- Add package.json with scripts and dependencies
- Add .eslintrc and prettier config"

# Commit 3: CI/CD
git add .github/workflows/
git commit -m "ci: [Phase-1] Set up GitHub Actions pipeline

- Add lint job
- Add build job
- Configure automated PR checks"

# Phase 1 complete - create tag
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure setup

Completed:
✓ Docker containerization for services
✓ Project structure and code formatting
✓ GitHub Actions CI/CD pipeline
✓ Local development environment"

# DAY 2: Phase 2 - Database Design
git checkout -b phase-2-database

git add migrations/ src/models/
git commit -m "feat: [Phase-2] Create database migrations

- Add migrations for users table
- Add migrations for notes table
- Add migrations for audit logs
- Configure migration runner"

# ... more Phase 2 commits ...

git tag -a phase-2 -m "Phase 2 Complete: Database schema

Completed:
✓ PostgreSQL schema design
✓ Database migrations setup
✓ ORM configuration
✓ Seed data for development"

# View your progress
git log --oneline --graph --all
# Shows:
# * <commit> Phase 2 work
# * <commit> Phase 2 work
# * <tag: phase-2>
# * <commit> Phase 1 work
# * <tag: phase-1>
# * <commit> Initial commit
```

---

## Troubleshooting

### "I forgot to tag Phase 1"
```bash
# Tag the current/past commit
git tag -a phase-1 -m "Phase 1 complete"
```

### "I need to change a tag message"
```bash
# Delete and recreate
git tag -d phase-1
git tag -a phase-1 -m "New message"
```

### "I want to go back to Phase 1"
```bash
git checkout phase-1    # View that phase's state
git checkout main       # Return to latest
```

### "What's the difference between two phases?"
```bash
git diff phase-1 phase-2 --stat
git diff phase-1 phase-2  # See actual code differences
```

### "How do I see commits in a phase?"
```bash
git log phase-1 --oneline
git log phase-0..phase-1 --oneline  # From phase-0 to phase-1
```

---

## Best Practices Checklist

Before creating a phase tag, verify:

- ✓ All checkpoint tasks are complete
- ✓ Code passes linting: `npm run lint`
- ✓ Tests pass: `npm run test`
- ✓ Build succeeds: `npm run build`
- ✓ README updated with phase progress
- ✓ Meaningful commit messages throughout
- ✓ No breaking changes from previous phase

---

## Git Configuration for Your Project

```bash
# View your config
git config --local --list

# If you need to change user
git config user.name "Your Name"
git config user.email "your.email@company.com"
```

---

## Pushing to Remote (GitHub/GitLab)

```bash
# Add remote repository
git remote add origin https://github.com/YOUR_USERNAME/mini-notes.git

# Push all commits
git push -u origin main

# Push all tags
git push origin --tags

# After phase-1 is tagged
git push origin phase-1  # Push single tag
git push origin --tags   # Push all tags
```

---

## Summary

1. **Create feature branch** (optional): `git checkout -b phase-1-setup`
2. **Make commits** throughout phase: `git commit -m "feat: [Phase-1] Description"`
3. **Verify checkpoint complete**: Run tests, check docs
4. **Tag the phase**: `git tag -a phase-1 -m "Description"`
5. **View progress**: `git log --oneline --graph --all`
6. **Repeat** for Phase 2, 3, etc.

Your Git repository now tracks **when each phase was completed** and you can always refer back to any phase's exact state!

---

## Need More Details?

See **GIT_WORKFLOW_GUIDE.md** for the complete guide with more examples and best practices.
