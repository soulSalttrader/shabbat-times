package il.soulSalttrader.shabbattimes.model

import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class City(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZone: ZoneId,
    val timeFormat: Int = 24,
    val candleLightingOffsetMinutes: Long = 20L,
    val havdalahOffsetMinutes: Long = 40L,
)