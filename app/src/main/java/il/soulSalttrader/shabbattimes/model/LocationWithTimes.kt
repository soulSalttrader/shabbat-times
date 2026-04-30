package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.toLabel

data class LocationWithTimes(
    val location: SavedLocation,
    val times: HalachicTimesDisplay?,
    val status: LocationStatus,
) {
    val label: String = status.toLabel()
}