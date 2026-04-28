package il.soulSalttrader.shabbattimes.model

data class LocationWithTimes(
    val location: SavedLocation,
    val times: HalachicTimesDisplay?,
)