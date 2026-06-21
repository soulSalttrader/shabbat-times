package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.UiText

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