# Mini-Notes Microservices Platform

A scalable microservices platform for note-taking with multi-service architecture, built with professional Git workflow practices.

## 📖 Quick Start

**New to this project?** Start here: [START_HERE.md](./START_HERE.md)

This guide explains:
- The Week 5 ground rule: "One Git repo, meaningful commits per phase. Tag each phase when its checkpoint passes (phase-1, phase-2, ...)."
- How to structure your commits
- When and how to create phase tags
- Daily workflow examples

## 🎯 Project Phases

| Phase | Focus | Status | Tag |
|-------|-------|--------|-----|
| **0** | Setup & Documentation | ✅ Complete | `phase-0` |
| **1** | Infrastructure (Docker, CI/CD) | 🎯 Next | `phase-1` |
| **2** | Database Schema & ORM | ⏳ Planned | `phase-2` |
| **3** | Core Microservices (Auth, Users, Notes) | ⏳ Planned | `phase-3` |
| **4** | API Gateway & Routing | ⏳ Planned | `phase-4` |
| **5** | Testing & CI/CD Pipeline | ⏳ Planned | `phase-5` |
| **6** | Deployment & Monitoring | ⏳ Planned | `phase-6` |

## 📚 Documentation

### Main Guides

- **[START_HERE.md](./START_HERE.md)** - Start here! Complete overview and quick start (10-min read)
- **[PHASE_WORKFLOW_QUICK_REF.md](./PHASE_WORKFLOW_QUICK_REF.md)** - Daily commands and patterns (quick reference)
- **[GIT_WORKFLOW_GUIDE.md](./GIT_WORKFLOW_GUIDE.md)** - Comprehensive guide with examples and best practices
- **[PHASE_CHECKLIST.md](./PHASE_CHECKLIST.md)** - Checkpoint verification for all 6 phases
- **[WORKFLOW_VISUALIZATION.txt](./WORKFLOW_VISUALIZATION.txt)** - Visual ASCII diagrams

### For Each Phase

Refer to **[PHASE_CHECKLIST.md](./PHASE_CHECKLIST.md)** for:
- Phase 1: Infrastructure setup tasks
- Phase 2: Database design tasks
- Phase 3: Microservices implementation
- Phase 4: API Gateway setup
- Phase 5: Testing and CI/CD
- Phase 6: Deployment and monitoring

## 🚀 Getting Started

### Prerequisites

- Git
- Node.js 18+
- Docker & Docker Compose
- npm or yarn

### Initialize Repository

```bash
# Clone or navigate to repo
cd mini-notes

# View current status
git log --oneline --graph --all

# View all tags
git tag -l
```

### Start Phase 1

```bash
# Create a phase branch
git checkout -b phase-1-setup

# Start adding Phase 1 files (Docker, structure, etc.)
# ... add your files ...

# Make meaningful commits
git add docker-compose.yml Dockerfile
git commit -m "feat: [Phase-1] Add Docker configuration

- Create Dockerfile for Node.js services
- Add docker-compose.yml with PostgreSQL and Redis
- Configure environment variables"

# When Phase 1 is complete and checkpoint verified
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure setup"
```

## 📝 Commit Message Format

Follow this pattern for all commits:

```
<type>: [Phase-N] <subject>

<body with bullet points describing changes>
```

**Types:**
- `feat:` - New feature
- `fix:` - Bug fix
- `docs:` - Documentation
- `chore:` - Configuration, dependencies
- `ci:` - CI/CD setup
- `test:` - Testing

**Example:**

```bash
git commit -m "feat: [Phase-1] Initialize project structure

- Create src/ with services/, gateway/, shared/ subdirectories
- Add package.json with npm scripts for dev/build/test
- Configure ESLint with airbnb config
- Configure Prettier for code formatting"
```

## 🏷️ Creating Phase Tags

When a phase's checkpoint passes:

```bash
# Create an annotated tag
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure setup

Completed:
✓ Docker containerization
✓ Project structure and linting
✓ CI/CD pipeline foundation
✓ Development environment documentation"

# View the tag
git show phase-1

# List all tags
git tag -l

# Push to remote
git push origin phase-1
git push origin --tags  # Push all tags
```

## 🔍 Viewing Progress

```bash
# See commits with phase tags
git log --oneline --graph --all

# See commits in a specific phase
git log phase-1 --oneline

# See changes between phases
git diff phase-1 phase-2 --stat

# Checkout a specific phase (view only)
git checkout phase-1

# Return to latest
git checkout master
```

## ✅ Checkpoint Verification

Before creating a phase tag, verify:

```bash
# Check code quality
npm run lint

# Run tests
npm run test

# Build succeeds
npm run build

# No vulnerabilities
npm audit
```

See **[PHASE_CHECKLIST.md](./PHASE_CHECKLIST.md)** for detailed checkpoint tasks per phase.

## 🛠️ Project Structure

```
mini-notes/
├── src/
│   ├── services/          # Microservices
│   ├── gateway/           # API Gateway
│   └── shared/            # Shared utilities
├── migrations/            # Database migrations
├── tests/                 # Test suites
├── deployment/            # Deployment config
├── .github/workflows/     # CI/CD pipelines
├── package.json           # Dependencies
├── docker-compose.yml     # Docker setup
└── docs/                  # Documentation
```

## 🔄 Workflow Example

**Day 1: Phase 1 Setup**

```bash
# Start phase
git checkout -b phase-1-setup

# Commit 1: Docker setup
git add Dockerfile docker-compose.yml
git commit -m "feat: [Phase-1] Add Docker configuration"

# Commit 2: Project structure
git add src/ package.json
git commit -m "feat: [Phase-1] Initialize project structure"

# Commit 3: CI/CD
git add .github/workflows/
git commit -m "ci: [Phase-1] Set up GitHub Actions"

# Phase 1 complete
git tag -a phase-1 -m "Phase 1 Complete: Infrastructure"
```

**Day 2: Phase 2 Database**

```bash
# Start next phase
git checkout -b phase-2-database

# Phase 2 commits...
git commit -m "feat: [Phase-2] Create database migrations"
git commit -m "feat: [Phase-2] Set up ORM"

# Phase 2 complete
git tag -a phase-2 -m "Phase 2 Complete: Database"
```

**View progress:**

```bash
git log --oneline --graph --all

# Shows clear timeline with tags marking each phase
```

## 📊 Git Commands Reference

### Daily Commands

```bash
# See current status
git status

# View commit history
git log --oneline -10
git log --oneline --graph --all

# Make a commit
git add <files>
git commit -m "feat: [Phase-N] Description"

# Create a phase tag
git tag -a phase-N -m "Phase N Complete: Description"

# View tags
git tag -l
git show phase-1

# Navigate phases
git checkout phase-1  # View phase 1
git checkout master   # Back to latest
```

### Remote (GitHub/GitLab)

```bash
# Add remote
git remote add origin https://github.com/YOUR_USERNAME/mini-notes.git

# Push commits
git push -u origin master

# Push tags
git push origin --tags
git push origin phase-1  # Single tag
```

## 🎓 Week 5 Ground Rule Implementation

This project implements the Week 5 challenge rule:

> **"One Git repo, meaningful commits per phase. Tag each phase when its checkpoint passes (phase-1, phase-2, ...)."**

**Implementation:**

✅ **One Git Repo** - All work in `/mini-notes`
✅ **Meaningful Commits** - Format: `feat: [Phase-N] Description`
✅ **Phase Tags** - `git tag -a phase-N -m "..."`
✅ **Checkpoint Verification** - Tag only when checkpoint passes
✅ **Clear History** - Git log shows project evolution

**Benefits:**

- 📍 Traceability: Know when each phase was completed
- 📝 Documentation: Commits explain what and why
- ✅ Quality: Tags mark verified checkpoints
- 🔄 Reversibility: Jump to any phase state
- 👔 Professionalism: Demonstrates DevOps practices

## ❓ Common Questions

**Q: How often should I commit?**
A: After completing a logical unit of work - typically 5-10 commits per phase.

**Q: What if I forgot to tag?**
A: You can retroactively tag: `git tag -a phase-1 -m "Description"`

**Q: Can I change a tag?**
A: Yes, delete and recreate:
```bash
git tag -d phase-1
git tag -a phase-1 -m "New message"
```

**Q: How do I see what changed between phases?**
A: 
```bash
git diff phase-1 phase-2 --stat  # Files changed
git diff phase-1 phase-2         # Actual changes
git log phase-1..phase-2         # Commits between
```

## 📞 Support & Resources

- **Git Documentation:** `git --help` or `git <command> --help`
- **Conventional Commits:** https://www.conventionalcommits.org/
- **Git Tagging:** https://git-scm.com/book/en/v2/Git-Basics-Tagging
- **Git Branching Model:** https://nvie.com/posts/a-successful-git-branching-model/

## 📋 Checklist Before Each Phase Tag

- [ ] All phase tasks complete (see PHASE_CHECKLIST.md)
- [ ] Tests pass: `npm run test`
- [ ] Linting passes: `npm run lint`
- [ ] Build succeeds: `npm run build`
- [ ] README/docs updated
- [ ] No breaking changes
- [ ] Meaningful commit messages throughout
- [ ] Ready to tag: `git tag -a phase-N -m "..."`

## 🚀 Next Steps

1. **Read:** [START_HERE.md](./START_HERE.md)
2. **Review:** [PHASE_WORKFLOW_QUICK_REF.md](./PHASE_WORKFLOW_QUICK_REF.md)
3. **Plan:** Phase 1 deliverables
4. **Start:** `git checkout -b phase-1-setup`
5. **Build:** Implement Phase 1
6. **Tag:** `git tag -a phase-1 -m "..."`
7. **Repeat:** For phases 2-6

---

**Happy coding! 🎉**

Remember: The goal is professional development practices with clear checkpoints and meaningful history.

For detailed information, see [START_HERE.md](./START_HERE.md).
