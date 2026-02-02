package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationStatus
import il.soulSalttrader.shabbattimes.location.LocationStatus.Distance
import kotlinx.serialization.Serializable

@Serializable
data class HalachicTimesDisplay(
    val city: City = Cities.JERUSALEM,
    val candleLightingTime: String = "",
    val candleLightingDate: String = "",
    val havdalahTime: String = "",
    val havdalahDate: String = "",
    val locationStatus: LocationStatus = Distance(9999),
)