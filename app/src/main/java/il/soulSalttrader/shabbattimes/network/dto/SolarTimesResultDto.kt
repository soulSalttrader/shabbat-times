package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesResultDto(
    val date: String = "",
    val sunset: String = "",
)