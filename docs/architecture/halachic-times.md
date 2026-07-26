# Halachic Times

## Calculation

Times are calculated using astronomical algorithms that determine solar altitude. 
For the underlying mathematical model, see [Ephemeris & Solar Position Engine](ephemeris-solar-position.md).

## Domain model

The HalachicTimes domain model encapsulates raw calculated times for a specific location.

```kotlin
data class HalachicTimes(
    val coordinates: Coordinates,
    val candleLighting: TimeState,
    val havdalah: TimeState,
)
```

## Display model

`HalachicTimesDisplay` is a UI-optimized model that handles nullability and formatting constants for the view layer.

```kotlin
@Immutable
@Serializable
data class HalachicTimesDisplay(
    val coordinates: Coordinates,
    val candleLighting: TimeState,
    val havdalah: TimeState,
) {
    companion object {
        const val EMPTY_TIME = "--:--"
        const val EMPTY_DATE = "dd/mm/yyyy"
        const val NA_TIME = "N/A"
    }
}
```

## Extensions

Helper functions facilitate model conversion and generate user-facing warnings for missing times.

```kotlin
fun HalachicTimes.toDisplay(): HalachicTimesDisplay = HalachicTimesDisplay(
    coordinates = coordinates,
    candleLighting = candleLighting,
    havdalah = havdalah,
)

fun List<HalachicTimes>.findForLocation(location: SavedLocation): HalachicTimes? =
    firstOrNull { it.coordinates == location.coordinates }

fun List<HalachicTimes>.toUnavailabilityWarning(): UiText? {
    val candleUnavailable = any { it.candleLighting is TimeState.Unavailable }
    val havdalahUnavailable = any { it.havdalah is TimeState.Unavailable }

    return when {
        candleUnavailable && havdalahUnavailable -> UiText.Resource(R.string.unavailable_both_snackbar)
        candleUnavailable                        -> UiText.Resource(R.string.unavailable_candle_lighting_snackbar)
        havdalahUnavailable                      -> UiText.Resource(R.string.unavailable_havdalah_snackbar)
        else                                     -> null
    }
}
```
