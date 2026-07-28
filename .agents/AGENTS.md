# Repo Agent Guide

This file serves as a human-readable overview of the agents and skills available in this repository.

> [!NOTE]
> **Primary Routing**: The Gemini/Antigravity CLI uses the YAML frontmatter in each `SKILL.md` file for automatic on-demand routing. This table is a fallback and reference guide for users.

## Available skills

| Trigger phrases                                                                               | Skill file |
|-----------------------------------------------------------------------------------------------|---|
| “/pr”, “compose PR”, “generate PR”, “write a PR”, “draft PR”, “PR description”, “/compose-pr” | `.agents/skills/pull-request/SKILL.md` |

## Adding a new skill

1. Create `.agents/skills/<name>/SKILL.md` with: Trigger, Input files, Rules, Template (same shape as the existing skills).
2. Add a row to the table above.
3. If the skill needs a script to produce input files, put it in `@./scripts/`. 
4. If it needs a driving prompt, put it in `@./assets/` - the prompt should just point at the SKILL.md and list which files to read, not restate the rules.
