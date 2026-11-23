# START HERE - Mini-Notes Git Phase Workflow

Welcome! This document explains how to use Git tags and meaningful commits to track your mini-notes project through its development phases.

## The Rule You're Implementing

From Week 5 Challenges:
> **"One Git repo, meaningful commits per phase. Tag each phase when its checkpoint passes (phase-1, phase-2, ...)."**

This means:
- ✅ All work in ONE Git repository
- ✅ Make meaningful commits for each piece of work (not "updates" or "stuff")
- ✅ Create a git tag when each phase's checkpoint passes
- ✅ Use semantic tags: `phase-1`, `phase-2`, etc.

---

## What You're Doing Differently

### ❌ WITHOUT This Workflow
```bash
git add .
git commit -m "updates"              # Vague
git commit -m "Phase 1 done"         # Unclear what was done

# Later: How do I see what was in Phase 1?
# Hard to know when phases started/ended
```

### ✅ WITH This Workflow
```bash
git add docker-compose.yml
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for microservices
- Add docker-compose.yml with PostgreSQL
- Configure environment variables"

git add src/
git commit -m "feat: [Phase-1] Initialize project structure

- Create service directories
- Add package.json with npm scripts
- Configure ESLint and Prettier"

# When Phase 1 is complete and checkpoint passes:
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure setup"

# Now you can easily:
# - See all Phase 1 commits: git log phase-1 --oneline
# - View Phase 1 state: git checkout phase-1
# - Diff phases: git diff phase-1 phase-2
# - See timeline: git log --oneline --graph --all
```

---

## Your 6-Phase Project Structure

Based on your MiniNotes Microservices Blueprint:

| Phase | Focus | Duration | Checkpoint |
|-------|-------|----------|-----------|
| **Phase 0** | ✓ DONE - Project setup and documentation | 1 day | Tag: `phase-0` |
| **Phase 1** | Infrastructure (Docker, CI/CD) | 3-5 days | Tag: `phase-1` |
| **Phase 2** | Database schema and ORM | 3-5 days | Tag: `phase-2` |
| **Phase 3** | Core microservices (Auth, Users, Notes) | 5-7 days | Tag: `phase-3` |
| **Phase 4** | API Gateway and routing | 3-5 days | Tag: `phase-4` |
| **Phase 5** | Testing and CI/CD pipeline | 5-7 days | Tag: `phase-5` |
| **Phase 6** | Deployment and monitoring | 5-7 days | Tag: `phase-6` |

---

## Quick Start - Your Workflow

### 1️⃣ Start Phase 1

```bash
cd C:\Users\lalainne.anne.j.abel\Documents\mini-notes
git checkout -b phase-1-setup
```

### 2️⃣ During Phase 1 - Make Meaningful Commits

```bash
# After completing a task
git add <files>
git commit -m "feat: [Phase-1] Your feature title

Detailed description:
- What you added
- Why you made this change
- Key implementation details"
```

**Common commit prefixes:**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `chore:` - Configuration, dependencies
- `ci:` - CI/CD setup
- `test:` - Testing

### 3️⃣ When Phase 1 Checkpoint Passes

Verify:
- ✓ All tasks complete
- ✓ Tests pass: `npm run test`
- ✓ Linting passes: `npm run lint`
- ✓ Build works: `npm run build`
- ✓ Documentation updated

Then create the tag:

```bash
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure setup

Completed:
✓ Docker containerization
✓ Project structure
✓ CI/CD pipeline
✓ Development environment docs"
```

### 4️⃣ Move to Phase 2

```bash
git checkout -b phase-2-database
```

Repeat the cycle!

---

## Understanding Git Tags

A **git tag** is like a bookmark that marks a specific commit as important.

```bash
# Create an annotated tag (recommended)
git tag -a phase-1 -m "Phase 1 Complete message"

# View the tag
git show phase-1

# List all tags
git tag -l

# Checkout to view that phase's code
git checkout phase-1

# Return to latest
git checkout main
```

**Why tags?**
- 🎯 Marks exact moment when phase was complete
- 📸 Captures the description of what was done
- 🔄 Easy to jump between phases
- 📊 Clear timeline in git history

---

## Documentation Files Created

You have 4 comprehensive guides in your repository:

### 📖 [GIT_WORKFLOW_GUIDE.md](./GIT_WORKFLOW_GUIDE.md)
Complete reference with:
- Detailed workflow explanation
- Example for all 6 phases
- Best practices
- Troubleshooting
- GitHub/GitLab integration

**When to read:** Deep dive, examples, best practices

### 🚀 [PHASE_WORKFLOW_QUICK_REF.md](./PHASE_WORKFLOW_QUICK_REF.md)
Quick reference with:
- One-page workflow patterns
- Common commands
- Real-world examples
- Quick troubleshooting

**When to read:** Daily work, quick lookup

### ✅ [PHASE_CHECKLIST.md](./PHASE_CHECKLIST.md)
Checkpoint verification for:
- All 6 project phases
- Specific tasks per phase
- Verification commands
- When to tag

**When to read:** Before tagging a phase, planning phase work

### 📊 [WORKFLOW_VISUALIZATION.txt](./WORKFLOW_VISUALIZATION.txt)
ASCII diagrams showing:
- Timeline view
- Git history example
- Step-by-step workflow
- Commands reference

**When to read:** Visual learner, understanding the flow

---

## Daily Workflow - Typical Day

```bash
# Morning: Start work on Phase 1
cd C:\Users\lalainne.anne.j.abel\Documents\mini-notes
git checkout phase-1-setup

# 10 AM: Completed Docker setup
git add Dockerfile docker-compose.yml
git commit -m "feat: [Phase-1] Add Docker configuration

- Created Dockerfile for Node.js services
- Added docker-compose.yml with PostgreSQL and Redis
- Configured environment variables for local dev"

# 2 PM: Completed project structure
git add src/ package.json
git commit -m "feat: [Phase-1] Initialize project structure

- Created microservices directory structure
- Added package.json with npm scripts
- Configured ESLint and Prettier"

# 4 PM: Set up CI/CD
git add .github/workflows/
git commit -m "ci: [Phase-1] Set up GitHub Actions pipeline

- Added lint job
- Added build job
- Configured automated testing"

# End of day: Check progress
git log --oneline --graph --all

# Shows:
# * <commit> ci: [Phase-1] Set up GitHub Actions
# * <commit> feat: [Phase-1] Initialize project structure
# * <commit> feat: [Phase-1] Add Docker configuration
# * <commit> docs: [Phase-0] Initialize documentation
```

---

## Common Questions

### Q: When should I make a commit?
**A:** After completing a logical unit of work. Usually:
- After implementing a feature
- After fixing a bug
- After updating documentation
- Multiple times per day (5-10 commits per phase)

### Q: What if I forgot to tag a phase?
**A:** No problem! You can tag it retroactively:
```bash
git tag -a phase-1 -m "Phase 1 complete"
```

### Q: Can I change a tag message?
**A:** Yes, delete and recreate:
```bash
git tag -d phase-1
git tag -a phase-1 -m "New message"
```

### Q: How do I see what changed between phases?
**A:** 
```bash
# See files that changed
git diff phase-1 phase-2 --stat

# See actual code differences
git diff phase-1 phase-2

# See commits between phases
git log phase-1..phase-2 --oneline
```

### Q: How do I work with branches and tags?
**A:** 
- **Branches** are for active work: `git checkout -b phase-1-setup`
- **Tags** are immutable markers: `git tag -a phase-1 ...`
- Tags point to specific commits, not moving like branches

### Q: Should I push tags to GitHub?
**A:** Yes! When you push to GitHub:
```bash
git push origin --tags  # Push all tags
git push origin phase-1  # Push single tag
```

---

## Example Phase 1 Commit Messages

Good examples to follow:

```bash
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile with Node.js 18 and Alpine
- Add docker-compose.yml with PostgreSQL 14, Redis 7
- Configure volumes for code hot-reload
- Set up environment file (.env.example)"

git commit -m "feat: [Phase-1] Initialize project structure

- Create src/ with services/, gateway/, shared/ subdirs
- Add package.json with npm scripts for dev/build/test
- Configure ESLint with airbnb config
- Configure Prettier for code formatting"

git commit -m "chore: [Phase-1] Update dependencies

- Upgrade Express from 4.17 to 4.18
- Add dotenv for environment variables
- Add winston for logging framework"

git commit -m "ci: [Phase-1] Set up GitHub Actions workflow

- Add lint job (npm run lint)
- Add build job (npm run build)
- Configure CI to run on push and PR
- Set up automatic testing"

git commit -m "docs: [Phase-1] Document local development setup

- Add setup instructions to README
- Document environment variables required
- Add troubleshooting common issues"
```

---

## Git Commands You'll Use Most

```bash
# Start a phase
git checkout -b phase-N-description

# Make commits (do this 5-10 times per phase)
git add <files>
git commit -m "feat: [Phase-N] Description"

# When phase is complete
git tag -a phase-N -m "Phase N Complete: description"

# View your work
git log --oneline --graph --all    # Pretty history
git tag -l                          # List all tags
git show phase-1                    # View tag details

# Navigate through phases (read-only)
git checkout phase-1                # View phase 1
git checkout main                   # Back to latest

# When ready to push (GitHub/GitLab)
git push origin main
git push origin --tags              # Push all tags
```

---

## Ready to Start?

You have:
- ✅ Git repository initialized
- ✅ Phase-0 tag created (this setup)
- ✅ Comprehensive documentation
- ✅ Clear checklist for each phase

**Next Step:**
```bash
git checkout -b phase-1-setup
# Start adding Docker files, project structure, etc.
# Make meaningful commits
# When complete, create: git tag -a phase-1 -m "..."
```

---

## Need Help?

1. **Quick reference?** → Read [PHASE_WORKFLOW_QUICK_REF.md](./PHASE_WORKFLOW_QUICK_REF.md)
2. **Detailed guide?** → Read [GIT_WORKFLOW_GUIDE.md](./GIT_WORKFLOW_GUIDE.md)
3. **Phase checklist?** → Read [PHASE_CHECKLIST.md](./PHASE_CHECKLIST.md)
4. **Visual explanation?** → Read [WORKFLOW_VISUALIZATION.txt](./WORKFLOW_VISUALIZATION.txt)
5. **Git command help?** → `git --help` or `git <command> --help`

---

**You're ready! Ship Phase 1! 🚀**

Remember: The goal is to demonstrate professional development practices by making meaningful commits and marking checkpoints with tags.

---

*Last updated: 2024*
*Git Workflow Version: 1.0*
