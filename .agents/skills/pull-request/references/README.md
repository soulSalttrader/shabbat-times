# Pull Request Skill - User Manual

`01-gather-context.sh → STOP (paths) → 02-split-diff.sh → STOP (pr-body.md) → 03-open-pr.sh`

This skill automates the creation of high-quality, split Pull Requests. 
It follows a multi-stage flow with intentional manual checkpoints to ensure accuracy and human oversight.

## How to trigger it

You can activate this skill using natural language or slash commands:
- **Slash Command**: Type `/pr` or `/compose-pr` in the chat.
- **Natural Language**: "Help me open a PR," "Summarize these changes for a PR," or "Draft a PR description."

## The Flow

### Step 1: Context Gathering
The agent runs `01-gather-context.sh` to fetch diffs, commit logs, and test stats.

### Step 2: Classification (Checkpoint 1)
The agent analyzes the changes and proposes a split:
- **MAIN**: Core logic and feature changes.
- **MINOR**: Mechanical changes (renames, formatting, dependency bumps).

**STOP**: The agent will show you these paths and wait for confirmation.
- Reply `continue` to proceed.
- Provide corrections (e.g., "move path/to/file to MINOR") if the split is incorrect.

### Step 3: Diff Splitting & Composition
Once confirmed, the agent:
1. Runs `02-split-diff.sh` to isolate the core changes.
2. Composes the PR body using the template at `@./templates/pull-request.md`.

### Step 4: Review & Submission (Checkpoint 2)
The agent displays the generated PR description in the chat.

**STOP**: Review the text for accuracy.
- Reply `open pr title: <Your Title>` to execute `03-open-pr.sh` and create the draft PR.
- Request any edits to the body before providing the final title.

## Design Philosophy

The **STOP** points are intentional and non-cosmetic:
- **Step 2 STOP** prevents the agent from generating a description based on a "polluted" diff (e.g., including 1000 lines of ktlint fixes in a feature summary).
- **Step 4 STOP** ensures no PR is opened without a final human sanity check on the AI-generated summary.

## Prerequisites
Ensure the agent has permission to execute shell scripts in your IDE settings. 
If prompted, allow the execution of the scripts located in `.agents/skills/pull-request/scripts/`.
