package il.soulSalttrader.shabbattimes.model

import kotlinx.serialization.Serializable

@Serializable
data class HalachicTimesDisplay(
    val city: City = Cities.JERUSALEM,
    val candleLightingTime: String = "",
    val candleLightingDate: String = "",
    val havdalahTime: String = "",
    val havdalahDate: String = "",
    val isFriday: Boolean = false,
    val isSaturday: Boolean = false,
)