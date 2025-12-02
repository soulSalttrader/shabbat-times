package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class HalachicTimes(
    val candleLightingTime: String = "",
    val candleLightingDate: String = "",
    val havdalahTime: String = "",
    val havdalahDate: String = "",
)