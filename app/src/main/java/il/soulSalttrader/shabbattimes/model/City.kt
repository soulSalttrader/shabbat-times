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
    val candleLightingOffsetMinutes: Long = 20L,
)