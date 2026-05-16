package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.toDisplayString
import java.time.format.DateTimeFormatter

fun HalachicTimes.toDisplay(): HalachicTimesDisplay = HalachicTimesDisplay(
    coordinates = coordinates,
    candleLightingTime = candleLightingTime.format(DateTimeFormatter.ofPattern("HH:mm")),
    candleLightingDate = candleLightingDate.toDisplayString(),
    havdalahTime = havdalahTime.format(DateTimeFormatter.ofPattern("HH:mm")),
    havdalahDate = havdalahDate.toDisplayString(),
)

fun List<HalachicTimes>.findForLocation(location: SavedLocation): HalachicTimes? =
    firstOrNull { it.coordinates == location.coordinates }