package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesResponseDto(
    val results: SolarTimesResultDto = SolarTimesResultDto(),
    val status: String = "",
)