package il.soulSalttrader.shabbattimes.model

import kotlinx.serialization.Serializable

@Serializable
data class HalachicTimesDisplay(
    val candleLightingTime: String = "",
    val candleLightingDate: String = "",
    val havdalahTime: String = "",
    val havdalahDate: String = "",
)