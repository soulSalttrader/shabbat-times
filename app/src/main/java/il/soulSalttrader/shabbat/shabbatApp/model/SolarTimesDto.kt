package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesDto(
    val results: SolarTimes = SolarTimes(),
    val status: String = "",
)