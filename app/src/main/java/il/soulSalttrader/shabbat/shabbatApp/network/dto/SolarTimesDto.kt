package il.soulSalttrader.retro.shabbatApp.network.dto

import il.soulSalttrader.retro.shabbatApp.network.dto.SolarTimes
import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesDto(
    val results: SolarTimes = SolarTimes(),
    val status: String = "",
)