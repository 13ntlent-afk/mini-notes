# Git Workflow Guide for Mini-Notes Project

## Week 5 Ground Rule: "One Git repo, meaningful commits per phase. Tag each phase when its checkpoint passes (phase-1, phase-2, ...)."

---

## Overview

This guide explains how to implement structured Git commits and semantic versioning tags in your mini-notes microservices project. This approach ensures:

- **Traceability**: Each phase is marked with a git tag
- **Clear History**: Meaningful commit messages show what was done in each phase
- **Easy Rollback**: Can quickly jump to any phase's state
- **Professional Workflow**: Demonstrates DevOps best practices

---

## Setup: Initialize Git Repository

If you haven't already:

```bash
cd C:\Users\lalainne.anne.j.abel\Documents\mini-notes
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"
```

---

## Phase Structure

Based on the MiniNotes Microservices Blueprint, your project likely has phases like:

- **Phase 1**: Project setup, infrastructure, and core dependencies
- **Phase 2**: Database schema and ORM setup
- **Phase 3**: Core microservices (Auth, Notes, Users services)
- **Phase 4**: API Gateway and routing
- **Phase 5**: Testing and CI/CD pipeline
- **Phase 6**: Deployment and monitoring

---

## Workflow for Each Phase

### 1. **Start a Phase**

Create a branch (optional but recommended):

```bash
git checkout -b phase-1-setup
```

### 2. **Make Meaningful Commits During the Phase**

Create commits that follow this pattern:

```bash
git add <files>
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for microservices
- Add docker-compose.yml for local development
- Configure volume mounts for code hot-reload"
```

**Commit Message Guidelines:**
- **Type**: `feat`, `fix`, `docs`, `chore`, `test`, `refactor`
- **Phase Reference**: Include `[Phase-N]` in the message
- **Concise Title**: First line should be clear (< 50 chars)
- **Detailed Body**: Explain *why* the change was made (use bullet points)

#### Examples:

```bash
# Good commit
git commit -m "feat: [Phase-1] Initialize project structure

- Create src/ directory with subdirectories for each microservice
- Add package.json with npm scripts
- Configure ESLint and Prettier for code formatting"

# Another good commit
git commit -m "feat: [Phase-2] Set up PostgreSQL schema

- Create migration files for users, notes, and audit tables
- Define relationships and constraints
- Add seed data for development"

# Bad commit (avoid these)
git commit -m "updates"  # Too vague
git commit -m "Phase 1"  # Doesn't describe what was done
```

### 3. **Complete Checkpoint Tasks**

Before tagging, verify that:

- All checkpoint tasks are completed
- Code passes linting and tests
- Documentation is updated
- No breaking changes are introduced

```bash
# Example checkpoint verification
npm run lint
npm run test
npm run build
```

### 4. **Tag the Phase When Checkpoint Passes**

Once all checkpoint tasks for a phase are complete:

```bash
# Lightweight tag (simple marker)
git tag phase-1

# OR Annotated tag (recommended - includes message and metadata)
git tag -a phase-1 -m "Phase 1: Project setup and infrastructure complete

Completed tasks:
- Docker configuration
- Project structure
- Initial CI/CD pipeline
- Development environment documentation"
```

**To list all tags:**
```bash
git tag -l
```

**To show tag details:**
```bash
git show phase-1
```

### 5. **Merge Back to Main Branch**

```bash
git checkout main
git merge phase-1-setup
```

---

## Example Workflow for Mini-Notes

Here's a complete example for Phase 1:

```bash
# Initialize and start Phase 1
cd C:\Users\lalainne.anne.j.abel\Documents\mini-notes
git init
git config user.name "Your Name"
git config user.email "your.email@example.com"

# Create initial commit
echo "# Mini-Notes Microservices" > README.md
git add README.md
git commit -m "docs: [Phase-1] Initialize project documentation"

# Phase 1: Setup
git add docker-compose.yml Dockerfile .dockerignore
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for Node.js microservices
- Add docker-compose.yml with PostgreSQL, Redis services
- Configure environment variables for local development"

git add src/
git commit -m "feat: [Phase-1] Initialize project structure

- Create microservices folder structure
- Add package.json with dependencies
- Set up ESLint and Prettier configurations"

git add .github/workflows/
git commit -m "ci: [Phase-1] Set up GitHub Actions pipeline

- Add lint job
- Add build job
- Configure automated testing on push"

# Phase 1 checkpoint passed - create tag
git tag -a phase-1 -m "Phase 1 Complete: Project infrastructure and setup

Completed:
✓ Docker containerization
✓ Project structure
✓ Linting and formatting rules
✓ CI/CD pipeline foundation"

# Move to Phase 2
git checkout -b phase-2-database

git add migrations/
git commit -m "feat: [Phase-2] Create database migrations

- Add migrations for users table
- Add migrations for notes table
- Add migrations for audit logs"

# ... continue with Phase 2 work ...

# When Phase 2 is complete
git tag -a phase-2 -m "Phase 2 Complete: Database schema and ORM setup

Completed:
✓ PostgreSQL schema design
✓ Database migrations
✓ Seed data for development
✓ Connection pooling configuration"
```

---

## Key Commands Reference

```bash
# View commit history
git log --oneline
git log --oneline --graph --all

# View tags
git tag -l
git show phase-1

# Create a tag
git tag -a phase-1 -m "Phase 1: Setup complete"

# Push tags to remote
git push origin phase-1
git push origin --tags  # Push all tags

# Checkout a specific phase
git checkout phase-1

# Create a release from a tag
git show phase-1  # View details before creating release

# Delete a tag (if needed)
git tag -d phase-1
git push origin --delete phase-1
```

---

## Best Practices

### ✅ DO:

1. **Commit Often**: Make commits after completing logical units of work
2. **Write Clear Messages**: Future you will appreciate it
3. **Tag at Checkpoints**: Create a tag when you've verified the phase is complete
4. **Use Branches**: Create a branch per phase for cleaner history
5. **Document Changes**: Include links to issues/PRs in commit messages (if applicable)
6. **Push Tags to Remote**: `git push origin --tags` to share with team

### ❌ DON'T:

1. **Large Commits**: Don't commit 50 files in one commit without explanation
2. **Vague Messages**: Avoid "updates", "bug fix", "stuff"
3. **Tag Incomplete Work**: Only tag when checkpoint is verified
4. **Force Push**: Avoid `git push --force` unless absolutely necessary
5. **Ignore Merge Conflicts**: Resolve them thoughtfully

---

## GitHub/GitLab Integration

If you push to GitHub or GitLab:

### Push your work:
```bash
git remote add origin https://github.com/YOUR_USERNAME/mini-notes.git
git branch -M main
git push -u origin main
git push origin --tags
```

### Create a Release:
On GitHub, go to **Releases** and click "Create release from tag":
- Select the phase tag (e.g., `phase-1`)
- Add release notes (copy from the tag message)
- Publish

---

## Verifying Your Workflow

After completing a phase:

```bash
# Check the commit history looks good
git log --oneline -10

# Verify the tag exists
git tag -l | grep phase

# Verify you can checkout to that phase
git checkout phase-1

# See what files changed in this phase
git diff phase-0 phase-1  # (or main..phase-1 if no phase-0)
```

---

## Troubleshooting

### "I forgot to tag Phase 1!"
```bash
# Tag the current/past commit retroactively
git tag -a phase-1 -m "Phase 1 complete"

# Or tag a specific commit
git tag -a phase-1 <commit-hash> -m "Phase 1 complete"
```

### "I need to modify a phase tag"
```bash
# Delete the old tag
git tag -d phase-1

# Create a new one
git tag -a phase-1 -m "Updated message"
```

### "What commits are in Phase 1?"
```bash
# View commits between phases
git log phase-1 --oneline

# View commits specific to a phase (if using branches)
git log phase-1-setup --oneline

# Compare two phases
git diff phase-1 phase-2 --stat
```

---

## Summary

By following this workflow:

1. ✅ Each phase has clear commit history
2. ✅ Phases are marked with git tags
3. ✅ You can easily revert to any phase state
4. ✅ Your repository demonstrates professional DevOps practices
5. ✅ Team members can understand the project evolution

**Next Step**: Initialize your git repo and start with Phase 1!

---

## Questions?

Refer to:
- [Git Tagging Guide](https://git-scm.com/book/en/v2/Git-Basics-Tagging)
- [Conventional Commits](https://www.conventionalcommits.org/)
- [Git Branching Model](https://nvie.com/posts/a-successful-git-branching-model/)
