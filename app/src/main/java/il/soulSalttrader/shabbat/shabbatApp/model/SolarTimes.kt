package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimes(
    val date: String = "",
    val sunset: String = "",
)