## Title
<!-- Use Conventional Commits format: type(scope): description. Common types: feat, fix, refactor, chore, docs. -->

> Example: 
> `feat(auth): add login with biometric`, `fix(ui): correct button alignment`

## Description
<!-- 2–4 sentences. Explain the 'Why' and the 'How it was': What problem does this solve? What was the previous behavior, and why is this approach better? Do not repeat the title. -->

> Example: 
> This PR introduces a significant architectural shift in how Shabbat times are calculated.
> We've moved away from community-based hardcoded presets to a robust astronomical engine for precise solar position logic.
> Additionally, the settings have been refactored to allow granular control over candle lighting offsets and Havdalah criteria.
> This approach robustly handles edge cases.

## Main Changes
<!-- From diff-main.txt only. Group by package/component. For each, describe the primary new responsibility or architectural shift. Avoid listing every method; focus on high-level impact. -->

> Example: 
> Key improvements include:
> Precise Ephemeris Engine: New package settings/ephemeris/ containing calculators for Equation of Time, Obliquity, and Mean Orbital Elements. 
> Solar Position Architecture: EphemerisBasedSolarPositionCalculator computes the Sun's altitude for any coordinate and timestamp, replacing simplified preset lookups.

## Minor Changes
<!-- From diff-minor.txt only. List incidental changes (e.g., formatting, dependency bumps, cleanup) that are OUTSIDE the main feature scope. One bullet per package/purpose. -->

> Example:
> Enhanced UI State: Location cards now feature descriptive status labels (Current, Nearby, etc.) and improved icon mapping for edge cases like "White Nights" (NA_TIME).

## Testing
<!-- List test files from diff-tests.txt. For Edge Cases and Devices, provide a placeholder like '[AUTHOR: Describe manual tests and device variants here]' to clearly signal that this section is incomplete without human input. -->

> Example:
> Unit Tests: Data-driven tests for NOAA ephemeris and solar position calculations. 
> Instrumentation: Refactored Permission and Connectivity flows verified with MockK-backed instrumented tests.
> Edge cases: [AUTHOR: Describe manual tests and device variants here]
> Devices/emulators: [AUTHOR: Describe manual tests and device variants here]
