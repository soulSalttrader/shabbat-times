package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimes(
    val date: String = "",
    val sunset: String = "",
)