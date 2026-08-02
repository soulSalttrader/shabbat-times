package il.soulSalttrader.shabbattimes.model

import java.time.ZoneId
import kotlinx.serialization.Serializable

@Serializable()
data class ResolvedLocation(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZoneId: ZoneId,
)
