package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesDto(
    val results: SolarTimes = SolarTimes(),
    val status: String = "",
)