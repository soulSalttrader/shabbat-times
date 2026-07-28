# PR Flow - Orchestrator

`01-gather-context.sh → STOP (paths) → 02-split-diff.sh → STOP (pr-body.md) → 03-open-pr.sh`

You drive an end-to-end PR-description pipeline. 

Follow the steps in order.

You have terminal access - actually run the allowed commands, do not just describe them.

## Step 1: Context

Run:
```zsh
./.agents/skills/pull-request/scripts/01-gather-context.sh
```

## Step 2: Classify → STOP

Follow the procedural guide at `@./references/classify.md` using the files from Step 1. Edit MAIN_PATHS in `@./scripts/02-split-diff.sh` accordingly.

**STOP** and print in chat:

- MAIN paths + one-line reason each
- MINOR paths + one-line reason each
- Anything you were unsure about

Then ask:
> Reply continue to proceed with these paths, or tell me what to change.

Do not run any further scripts until I respond.

## Step 3: Split (only after continue or corrections)

If I gave corrections, apply them to MAIN_PATHS in `@./scripts/02-split-diff.sh` first.

Then run:
```zsh
./.agents/skills/pull-request/scripts/02-split-diff.sh
```

## Step 4: Compose PR body → STOP

Follow the prompt template at `@./assets/compose-pr.md` using the files produced so far.

Write the result to `@.pr-context/pr-body.md`.

Print the full contents of `@.pr-context/pr-body.md` in chat.

Then ask:
> Reply open pr title: <your title> to run @03-open-pr.sh, or tell me what to change in the body.

Do not run 03-open-pr.sh unless I explicitly instruct you with a title.

## Rules

- Never run 03-open-pr.sh without an explicit instruction that includes a PR title.
- Never skip the Step 2 checkpoint, even if classification seems obvious.
- If any script fails, print the error and stop. Do not attempt silent workarounds.
- 