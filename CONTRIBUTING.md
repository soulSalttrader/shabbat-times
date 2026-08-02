# Contributing

Thanks for your interest in this project.

**Note**: This project is **not open source**. It is distributed under an all-rights-reserved license (see [LICENSE](LICENSE.md)).

I'm not actively seeking external contributors at this time, but bug reports and feature suggestions are always welcome.

### What is accepted

- Bug reports via GitHub Issues
- Feature suggestions
- Pull requests (PRs) — but they may not be reviewed or merged

### Important

By submitting a pull request, you agree that your contribution becomes part of the project and is licensed under the same **all-rights-reserved** terms as the rest of the codebase.
You grant the maintainer a perpetual, irrevocable license to use, modify, and distribute your contribution.

### Getting Started (for reference)

1. Fork the repository
2. Create a branch (`feat/something` or `fix/something`)
3. Make your changes
4. Ensure tests pass
5. Open a PR against the `develop` branch

See [README.md](README.md) for setup and build instructions.

> Promotion from `develop` to `main` is handled separately as part of release planning.

## Code style
- Follows the [Kotlin official style guide](https://kotlinlang.org/docs/coding-conventions.html), with a few project-specific exceptions - see [docs/CODE_STYLE_EXCEPTIONS.md](docs/CODE_STYLE_EXCEPTIONS.md)
- Run `./gradlew ktlintCheck` before committing (fails the build on style violations; use `./gradlew ktlintFormat` to auto-fix most issues)
- Pure formatting commits are excluded from `git blame` via `.git-blame-ignore-revs`
  - run `git config blame.ignoreRevsFile .git-blame-ignore-revs` once after cloning to enable this locally

> Keep commits focused and messages descriptive

## Architecture
Please read [ARCHITECTURE.md](docs/ARCHITECTURE.md) before making structural changes.

## Testing
- Unit tests: `./gradlew test`
- Instrumented tests: `./gradlew connectedAndroidTest`

> Make sure both pass before opening a PR

---

**Questions?** Feel free to open an issue.
