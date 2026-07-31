# Code Style Exceptions

This project uses [ktlint](https://github.com/pinterest/ktlint) to enforce consistent Kotlin style,
configured via `.editorconfig` at the repo root. Most of ktlint's default rules are kept as-is,
but a few are disabled because they conflict with common, idiomatic Android/Kotlin patterns or reduce
readability without a clear benefit.

This file documents *why* each exception exists, so contributors (and future us)
aren't left guessing.

## `ktlint_standard_annotation` - disabled

**What it normally enforces:** every annotation must be on its own line, above the declaration it applies to.

**Why it's disabled:** this breaks a very common and readable Dagger/Hilt idiom -
  keeping `@Inject` directly above `constructor` on the same line:

```kotlin
// Allowed here (rule disabled):
class ResolveGpsLocationUseCase @Inject constructor(
    private val locationProvider: LocationProvider,
)

// What the rule would otherwise force:
class ResolveGpsLocationUseCase
    @Inject
    constructor(
        private val locationProvider: LocationProvider,
    )
```

The first form is the standard convention in most DI-heavy Kotlin codebases and is what Android Studio's
own formatter produces by default.

**Trade-off:** this rule is disabled for *all* annotations, not just `@Inject` - ktlint doesn't support
per-annotation exceptions. If you want another annotation (e.g. a `@Composable` function) on its own line
for readability, that's now a manual style choice rather than something ktlint enforces.

## `ktlint_standard_blank-line-before-declaration` - disabled

**What it normally enforces:** a blank line before every declaration in a class/interface, even short, closely-related ones.

**Why it's disabled:** it fights against compact, grouped declarations - for example,
sealed interface members that are simple and clearly belong together:

```kotlin
// Allowed here:
sealed interface PermissionState : State {
    data object Idle : PermissionState
    data object Education : PermissionState
    data object Granted : PermissionState
}

// What the rule would otherwise force:
sealed interface PermissionState : State {
    data object Idle : PermissionState

    data object Education : PermissionState

    data object Granted : PermissionState
}
```

## `ktlint_standard_spacing-between-declarations-with-annotations` - disabled

**What it normally enforces:** a blank line before any declaration that carries an annotation, even trivial one-liners.

**Why it's disabled:** same reasoning as above - short, related, annotated properties read better grouped together:

```kotlin
// Allowed here:
interface SettingsOption {
    @get:StringRes val titleRes: Int
    @get:StringRes val descRes: Int
}

// What the rule would otherwise force:
interface SettingsOption {
    @get:StringRes val titleRes: Int

    @get:StringRes val descRes: Int
}
```

## `ktlint_standard_blank-line-between-when-conditions` - disabled

**What it normally enforces:** once *any* branch in a `when` block has a multiline condition, blank lines are
required between *all* branches - including short one-liners.

**Why it's disabled:** too aggressive for `when` blocks where most branches are short and only one happens to wrap.
We'd rather keep those compact.

## `ktlint_standard_multiline-expression-wrapping` - disabled

**What it normally enforces:** if a named-argument value is itself a multiline call, the value must start on
its own new line after `=`.

**Why it's disabled:** the forced style adds an unnecessary indent level and separates the call from its assignment:

```kotlin
// Allowed here:
getHalachicTimes(
    startEvent = SolarTimesRequest(
        date = shabbatCalendar.upcomingCandleLightingDate(),
        coordinates = location.coordinates,
        timeZone = location.timeZoneId,
    ),
)

// What the rule would otherwise force:
getHalachicTimes(
    startEvent =
        SolarTimesRequest(
            date = shabbatCalendar.upcomingCandleLightingDate(),
            coordinates = location.coordinates,
            timeZone = location.timeZoneId,
        ),
)
```

## `ktlint_standard_class-signature` - disabled project-wide

**What it normally enforces:** several formatting decisions about class headers - among them, that a class's
supertype must start on a new line after `:`, and that a constructor with only a single parameter must collapse
onto one line (no wrapping, no trailing comma) even if constructors with 2+ parameters are wrapped multiline.

**Why it's disabled:** this rule caused two separate, unrelated problems, both times fighting a style we actually prefer:

1. It breaks the idiomatic Kotest trailing-lambda spec pattern:

```kotlin
// Allowed here:
class AstronomicalSolarDepressionCalculatorTest : DescribeSpec({
    // spec body
})

// What the rule would otherwise force:
class AstronomicalSolarDepressionCalculatorTest :
    DescribeSpec({
        // spec body
    })
```

2. It forces single-parameter constructors onto one line while multi-parameter constructors stay wrapped -
   creating inconsistent formatting across the codebase based purely on parameter count:

```kotlin
// Allowed here - consistent with multi-param constructors elsewhere:
class GetLocationSuggestionsUseCase @Inject constructor(
    private val geocodingRepository: GeocodingRepository,
) {

// What the rule would otherwise force:
class GetLocationSuggestionsUseCase @Inject constructor(private val geocodingRepository: GeocodingRepository) {
```

We'd rather have one predictable rule ("constructor params always multiline with trailing comma")
than have formatting silently change based on parameter count.

## `ktlint_standard_function-signature` - disabled

**What it normally enforces**: if a function's parameter list fits within max_line_length on one line, it must be collapsed onto a single line - even if it was written multiline with a trailing comma.

**Why it's disabled**: this overrides deliberate multiline formatting purely based on character count,
creating the same kind of inconsistency as class-signature's single-param collapse - a function stays multiline or
collapses depending on how close it happens to sit to the line length limit, not based on what's actually more readable:

```kotlin
// Allowed here:
private fun LeadingSearchIcon(
    resId: Int = R.drawable.search_24px,
    contentDescription: String? = "search_leading",
) {

// What the rule would otherwise force:
private fun LeadingSearchIcon(resId: Int = R.drawable.search_24px, contentDescription: String? = "search_leading") {
```

## `ktlint_standard_no-wildcard-imports` - disabled

**What it normally enforces:** every import must be explicit; wildcard imports (e.g. `import java.util.*`) are forbidden.

**Why it's disabled:** we allow common wildcard imports for packages like `java.util.*` which are standard in Android development and reduce clutter when using many collection types or utilities from the same package.

```kotlin
// Allowed here:
import java.util.*

// What the rule would otherwise force:
import java.util.Calendar
import java.util.Date
import java.util.Locale
```

## `ktlint_standard_property-naming` - disabled

**What it normally enforces:** properties must follow camelCase naming conventions.

**Why it's disabled:** this allows PascalCase for properties that hold Composable lambdas, maintaining consistency with Composable function naming conventions in Jetpack Compose.

```kotlin
// Allowed here:
val PrimaryButtonAction = @Composable {
    Button(onClick = {}) { Text("Click Me") }
}

// What the rule would otherwise force:
val primaryButtonAction = @Composable {
    Button(onClick = {}) { Text("Click Me") }
}
```

## `ktlint_standard_filename` - disabled

**What it normally enforces:** the filename must match the name of the top-level class or object defined within it.

**Why it's disabled:** this allows for utility files or files containing multiple related small components (like a set of related Composable previews or UI models) to have descriptive names that don't necessarily match a specific class.

```kotlin
// Allowed in a file named "ShabbatUiModels.kt":
data class ShabbatEntry(val id: String)
data class ShabbatHeader(val title: String)

// What the rule would otherwise force:
// The file would have to be named "ShabbatEntry.kt" if it was the primary class.
```

> While this rule is disabled to allow specific compact layouts, we still manually prefer the "one argument per line" style for standard UI calls with multiple simple arguments (like the `Text` example above).

## `ktlint_standard_function-naming` - disabled for `**/*Test.kt` only

**What it normally enforces:** function names must follow camelCase conventions.

**Why it's disabled:** it allows for descriptive, back-ticked test names which are idiomatic in Kotlin testing for improving the readability of test reports.

```kotlin
// Allowed here:
@Test
fun `given user is logged in when they open the profile screen then the correct data is displayed`() {
    // test body
}

// What the rule would otherwise force:
@Test
fun givenUserIsLoggedInWhenTheyOpenTheProfileScreenThenTheCorrectDataIsDisplayed() {
    // test body
}
```

---

> [!NOTE]
> If you think one of these exceptions should be reconsidered, or you've hit another rule that seems worth
> adding to this list, open an issue or PR updating both `.editorconfig` and this document together.
