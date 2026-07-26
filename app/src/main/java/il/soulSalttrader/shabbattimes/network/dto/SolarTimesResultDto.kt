package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SolarTimesResultDto(
    val date: String = "",
    val sunset: String = "",
    val dusk: String = "",
    @SerialName("nautical_twilight_end") val nauticalTwilightEnd: String = "",
    @SerialName("utc_offset") val utcOffsetMinutes: Int = 0,
)