package il.soulSalttrader.shabbattimes.model

fun HalachicTimes.toDisplay(): HalachicTimesDisplay = HalachicTimesDisplay(
    coordinates = coordinates,
    candleLighting = candleLighting,
    havdalah = havdalah,
)

fun List<HalachicTimes>.findForLocation(location: SavedLocation): HalachicTimes? =
    firstOrNull { it.coordinates == location.coordinates }