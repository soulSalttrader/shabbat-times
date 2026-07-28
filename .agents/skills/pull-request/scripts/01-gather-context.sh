#!/usr/bin/env bash
# Step 1: gather cheap, always-safe context for PR generation.
# Usage: ./01-gather-context.sh [base-branch]
# Default base branch is develop.

set -e

# Detect default branch if not provided
if [[ -z "$1" ]]; then
  BASE_BRANCH=$(git remote show origin | grep 'HEAD branch' | cut -d' ' -f5)
else
  BASE_BRANCH="$1"
fi

OUT_DIR=".pr-context"

rm -f "${OUT_DIR}"/*.txt 2>/dev/null || true
mkdir -p "${OUT_DIR}"

git fetch origin "${BASE_BRANCH}" --quiet

git diff "origin/${BASE_BRANCH}...HEAD" --stat > "${OUT_DIR}/diff-stat-all.txt"
git log "origin/${BASE_BRANCH}..HEAD" --oneline > "${OUT_DIR}/commits.txt"

echo "Context ready in ${OUT_DIR}/:"
echo "  - diff-stat-all.txt"
echo "  - commits.txt"
echo ""
echo "Next: run classify.md in Gemini, pointing it at these two files."
echo "Gemini should edit 02-split-diff.sh with MAIN_PATHS / EXCLUDES, then you run it."
