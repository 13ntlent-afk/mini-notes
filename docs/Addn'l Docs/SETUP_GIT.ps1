# PowerShell script to initialize Git repository with phase workflow
# Run this once to set up your mini-notes Git repository

# Change to project directory
$projectPath = "C:\Users\lalainne.anne.j.abel\Documents\mini-notes"
Set-Location $projectPath

Write-Host "=====================================" -ForegroundColor Cyan
Write-Host "Mini-Notes Git Workflow Setup" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

# Check if git is already initialized
if (Test-Path '.git') {
    Write-Host "`n✓ Git repository already exists" -ForegroundColor Green
} else {
    Write-Host "`nInitializing git repository..." -ForegroundColor Yellow
    git init
    Write-Host "✓ Git repository initialized" -ForegroundColor Green
}

# Configure git user (optional - modify as needed)
Write-Host "`nConfiguring git user..." -ForegroundColor Yellow

# Check if name is already configured
$gitName = git config user.name
if ([string]::IsNullOrEmpty($gitName)) {
    # Prompt for user info
    $userName = Read-Host "Enter your name (default: 'Developer')"
    if ([string]::IsNullOrEmpty($userName)) { $userName = "Developer" }
    git config user.name $userName
}

$gitEmail = git config user.email
if ([string]::IsNullOrEmpty($gitEmail)) {
    $userEmail = Read-Host "Enter your email (default: 'dev@mini-notes.local')"
    if ([string]::IsNullOrEmpty($userEmail)) { $userEmail = "dev@mini-notes.local" }
    git config user.email $userEmail
}

Write-Host "✓ Git configured" -ForegroundColor Green
Write-Host "  User: $(git config user.name)" -ForegroundColor Green
Write-Host "  Email: $(git config user.email)" -ForegroundColor Green

# Create initial files if they don't exist
Write-Host "`nChecking for initial files..." -ForegroundColor Yellow

if (-not (Test-Path 'README.md')) {
    $readmeContent = @'
# Mini-Notes Microservices Platform

A scalable microservices platform for note-taking with multi-service architecture.

## Project Structure

- Phase 1: Project setup and infrastructure
- Phase 2: Database schema and ORM
- Phase 3: Core microservices (Auth, Notes, Users)
- Phase 4: API Gateway and routing
- Phase 5: Testing and CI/CD
- Phase 6: Deployment and monitoring

## Getting Started

```bash
docker-compose up
```

For detailed workflow information, see [GIT_WORKFLOW_GUIDE.md](./GIT_WORKFLOW_GUIDE.md)

## Phases

- phase-1: Setup and infrastructure
- phase-2: Database design
- phase-3: Microservices
- phase-4: API Gateway
- phase-5: Testing and CI/CD
- phase-6: Deployment
'@
    $readmeContent | Out-File -FilePath README.md -Encoding UTF8
    Write-Host "✓ Created README.md" -ForegroundColor Green
} else {
    Write-Host "✓ README.md already exists" -ForegroundColor Green
}

if (-not (Test-Path '.gitignore')) {
    $gitignoreContent = @'
# Dependencies
node_modules/
package-lock.json
yarn.lock

# Environment variables
.env
.env.local
.env.*.local

# IDE
.vscode/
.idea/
*.swp
*.swo
*~

# Logs
logs/
*.log
npm-debug.log*

# Build outputs
dist/
build/
out/

# OS
.DS_Store
Thumbs.db

# Docker
.docker/
'@
    $gitignoreContent | Out-File -FilePath .gitignore -Encoding UTF8
    Write-Host "✓ Created .gitignore" -ForegroundColor Green
} else {
    Write-Host "✓ .gitignore already exists" -ForegroundColor Green
}

# Create initial commit if no commits exist
$commitCount = git rev-list --count HEAD 2>$null
if ($commitCount -eq 0) {
    Write-Host "`nCreating initial commit..." -ForegroundColor Yellow
    git add README.md .gitignore GIT_WORKFLOW_GUIDE.md
    git commit -m "docs: [Phase-0] Initialize project documentation

- Create README with project overview
- Add .gitignore for Node.js and development tools
- Add Git workflow guide for phase-based development"
    Write-Host "✓ Initial commit created" -ForegroundColor Green
} else {
    Write-Host "`n✓ Repository already has commits" -ForegroundColor Green
}

# Create .gitkeep for empty directories
$directories = @(
    "src",
    "src/services",
    "src/gateway",
    "src/shared",
    "migrations",
    "tests",
    "docs",
    "deployment",
    ".github/workflows"
)

Write-Host "`nSetting up directory structure..." -ForegroundColor Yellow
foreach ($dir in $directories) {
    if (-not (Test-Path $dir)) {
        New-Item -ItemType Directory -Path $dir -Force | Out-Null
        # Create .gitkeep to track empty directories
        New-Item -ItemType File -Path "$dir/.gitkeep" -Force | Out-Null
        Write-Host "✓ Created $dir/" -ForegroundColor Green
    }
}

# Display workflow summary
Write-Host "`n=====================================" -ForegroundColor Cyan
Write-Host "Setup Complete!" -ForegroundColor Cyan
Write-Host "=====================================" -ForegroundColor Cyan

Write-Host @"
Your Git repository is ready for phase-based development.

Next steps:

1. Start Phase 1 development:
   git checkout -b phase-1-setup

2. Make meaningful commits:
   git commit -m "feat: [Phase-1] Your feature description"

3. When Phase 1 checkpoint passes:
   git tag -a phase-1 -m "Phase 1 Complete: Setup and infrastructure"

4. View your progress:
   git log --oneline --graph --all
   git tag -l

Refer to GIT_WORKFLOW_GUIDE.md for detailed instructions.
"@ -ForegroundColor Cyan

Write-Host "`nYour Git Status:" -ForegroundColor Yellow
git status

Write-Host "`nGit Configuration:" -ForegroundColor Yellow
git config --local --list | Select-String "user\.|core\."
