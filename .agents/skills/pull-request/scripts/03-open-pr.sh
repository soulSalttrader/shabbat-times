#!/usr/bin/env bash
# Step 3: open the PR as a draft, using a body file you (or Gemini) prepared.
# Usage: ./03-open-pr.sh "PR Title" path/to/pr-body.md [base-branch]

set -e

TITLE="$1"
BODY_FILE="$2"
BASE_BRANCH="${3:-develop}"

if [[ -z "$TITLE" || -z "$BODY_FILE" ]]; then
  echo "Usage: $0 \"PR Title\" path/to/pr-body.md [base-branch]"
  exit 1
fi

if [[ ! -f "$BODY_FILE" ]]; then
  echo "Body file not found: $BODY_FILE"
  exit 1
fi

CURRENT_BRANCH=$(git rev-parse --abbrev-ref HEAD)

git push -u origin "$CURRENT_BRANCH"

gh pr create \
  --base "$BASE_BRANCH" \
  --head "$CURRENT_BRANCH" \
  --title "$TITLE" \
  --body-file "$BODY_FILE" \
  --draft

echo "Draft PR opened: $CURRENT_BRANCH -> $BASE_BRANCH"
echo "Review it, then mark ready for review when satisfied."
