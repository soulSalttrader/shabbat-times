package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.LocationWithTimes

data class LocationWithTimesUi(
    val locationWithTimes: LocationWithTimes,
    val status: LocationStatus,
) {
    val label: String = status.toLabel()
}