---
name: pull-request
description: |
  Expert at creating, improving, and refining Pull Request (PR) or Merge Request (MR) descriptions and titles.
  
  Triggers:
  - Explicit: "compose PR", "generate PR", "/pr", "/compose-pr", "PR description", "PR body", "PR title"
  - Natural Language: "help me open a PR", "write the PR for this", "summarize these changes for a PR", "improve my PR text"
  - Artifacts: Pasting a diff/commits and asking for a summary/title/PR body.
  - Variants: GitHub PR, GitLab MR, Merge Request.

  Negative Triggers:
  - Do NOT trigger for pure code review, commit-message-only requests, or general Git questions (e.g., "how to rebase") that do not involve drafting PR text.
---

## Input files

You do NOT have direct git access. 

The user provides three prepared files as your only source of truth:

1. **commits.txt** - commit log (`git log origin/develop..HEAD --oneline`).
   Use this to infer the change type (feat / fix / refactor / chore / etc.) for the Conventional Commits title.

2. **diff-main.txt** - full diff content of the core feature and all files it directly depends on (e.g. related repositories, composables, viewmodels).
   This covers only the paths classified as MAIN (see the PR Splitting Guidelines below).
   Treat this as the complete picture of *what changed and why* - this is your primary source for the PR body.

3. **diff-minor.txt** - stat-only summary of ALL incidental changes (everything NOT in MAIN or TESTS).
   Use this to find the "minor" scope: docs, unrelated cleanup, renames, formatting, dependency bumps, etc.
   Mention these briefly - one short line per package (e.g. "also updates docs in `feat/payments`"), never to describe implementation detail you can't see (you only have filenames + line counts for these, not their content).

4. **diff-tests.txt** - stat-only summary (filenames only) of test files touched (androidTest, test, *Test.kt).
   Use this only to name which test files were added/changed. Do NOT infer test coverage, edge cases, or devices tested from this - those must come from the user directly.

## Rules

1. Base every claim strictly on what's present in these four files.
2. Do not invent behavior, file names, edge cases, or devices tested that aren't evidenced by the diffs - if something is ambiguous or not present in the diffs, flag it as missing/assumption rather than stating it as fact.
3. Determine the Conventional Commit type/scope from commits.txt first; fall back to diff-main.txt if commit messages are inconsistent or missing.
4. Write the PR body from diff-main.txt. Use diff-minor.txt for the minor-changes list and mention it briefly - a single line or bullet per package, not in the same depth as the main changes.
5. For the Testing section, list test files from diff-tests.txt by name. Leave "Edge Cases" and "Tested on [devices/emulators]" as explicit placeholders for the user to fill in - do not fabricate.
6. Follow the exact section structure of the template below - don't add or remove sections.
7. Be concise but informative.
8. Explicitly call out breaking changes, architecture decisions, and testing impact if present in the diffs.

## PR Splitting Guidelines

Before this stage, changed paths in the branch are classified into MAIN and MINOR groups (commit messages are the strongest signal - see `@./references/classify.md` for the exact process).
diff-main.txt is the full diff for MAIN paths only.
This classification step is separate from PR writing and may already be done by the time you're asked to write the PR body - if `@.pr-context/diff-main.txt` already exists, assume the split is already correct and just write from it.
If you need to perform classification, refer to the procedural guide at `@./references/classify.md`.

## Template

Read the file at `@./templates/pull-request.md` in this repo and follow its section structure exactly - don't add or remove sections.