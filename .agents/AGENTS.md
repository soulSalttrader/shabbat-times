# AGENTS.md

An Android app that fetches solar data from an API, computes ephemeris and solar position from it,
and determines accurate Shabbat candle-lighting (start) and Havdalah (end) times for the user.

## What this project uses

- **Shared skills (via skill-kit):** `.agents/skills/`

## Skill index

| Skill          | Source                        | Version pinned |
|----------------|-------------------------------|----------------|
| `pull-request` | `.agents/skills/pull-request` | `v0.1.0`       |

> [!IMPORTANT]
> This table is an authorization list, not just a reference - treat a skill NOT listed here as
> unavailable even if its files are present on disk.

## Guardrails

- **Always:** [safe, reversible actions - e.g. "run tests," "create a branch"]
- **Ask first:** [state-changing actions - e.g. "open a PR," "change CI config"]
- **Never:** [hard boundaries - e.g. "force-push to
  `main`," "commit secrets," "rewrite shared branch history"]

## Project commands

```bash
./gradlew build
./gradlew test
./gradlew connectedAndroidTest
./gradlew lint
./gradlew ktlintCheck
```

## Setup

```bash
git submodule update --init --recursive
```

Full install, update, removal, and troubleshooting steps - including per-skill setup requirements -
live in INSTALL.md. This command is the one exception kept here: a quick self-heal an agent can run
directly when skills are missing, without a file hop.

## Other tools (CLAUDE.md, GEMINI.md, etc.)

Some tools (Claude Code, Gemini CLI) don't read `AGENTS.md` by default and need a small bridge file;
others read it natively. Exact per-tool steps - including which tools need only a bridge file vs.
also need skill-discovery wiring - live in WIRING.md.

> [!NOTE]
> Don't rename this file to `CLAUDE.md` or `GEMINI.md` and drop `AGENTS.md` - that trades a
> broadly-supported standard for a single-vendor one and brings back per-tool duplication. Keep
`AGENTS.md` as the source of truth; add thin bridge files only for tools that need one.
