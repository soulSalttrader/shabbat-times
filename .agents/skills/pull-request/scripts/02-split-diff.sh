#!/usr/bin/env bash
# Step 2: produce the full diff for the MAIN change, and stat-only diffs
# for everything else + tests.
#
# >>> GEMINI: edit MAIN_PATHS below based on classify.md output.
# >>> Do not edit anything else in this file unless asked.

set -e

# Detect default branch if not provided
if [[ -z "$1" ]]; then
  BASE_BRANCH=$(git remote show origin | grep 'HEAD branch' | cut -d' ' -f5)
else
  BASE_BRANCH="$1"
fi

OUT_DIR=".pr-context"

# --- EDIT THIS ARRAY PER PR ---------------------------------------------
MAIN_PATHS=(
  '.agents/skills/pull-request/SKILL.md'
  '.agents/skills/pull-request/assets/compose-pr.md'
  '.agents/skills/pull-request/assets/run-pr-flow.md'
  '.agents/skills/pull-request/scripts/01-gather-context.sh'
  '.agents/skills/pull-request/scripts/02-split-diff.sh'
  '.agents/skills/pull-request/scripts/03-open-pr.sh'
  '.agents/skills/pull-request/templates/pull-request.md'
)
# -------------------------------------------------------------------------

TEST_PATHS=(
  '**/androidTest/**'
  '**/test/**'
  '**/*Test.kt'
)

mkdir -p "${OUT_DIR}"

# Full diff of the main/significant change only.
git diff "origin/${BASE_BRANCH}...HEAD" -- "${MAIN_PATHS[@]}" \
  > "${OUT_DIR}/diff-main.txt"

# Stat diff of minor changes (everything NOT in MAIN or TEST).
# This makes it easier for the AI to list incidental changes reliably.
EXCLUDE_ARGS=()
for p in "${TEST_PATHS[@]}"; do EXCLUDE_ARGS+=(":(exclude)$p"); done
for p in "${MAIN_PATHS[@]}"; do EXCLUDE_ARGS+=(":(exclude)$p"); done

git diff "origin/${BASE_BRANCH}...HEAD" --stat -- "${EXCLUDE_ARGS[@]}" \
  > "${OUT_DIR}/diff-minor.txt"

# Stat-only diff for test files (context only, not full diff).
git diff "origin/${BASE_BRANCH}...HEAD" --stat -- "${TEST_PATHS[@]}" \
  > "${OUT_DIR}/diff-tests.txt"

echo "Wrote:"
echo "  - ${OUT_DIR}/diff-main.txt   (full diff, MAIN_PATHS only)"
echo "  - ${OUT_DIR}/diff-minor.txt  (stat only, everything else)"
echo "  - ${OUT_DIR}/diff-tests.txt  (stat only, test files)"
echo ""
echo "Next: run compose-pr.md in Gemini, pointing it at diff-main.txt, diff-minor.txt,"
echo "diff-tests.txt, and commits.txt. Review the PR body, then run 03-open-pr.sh."
