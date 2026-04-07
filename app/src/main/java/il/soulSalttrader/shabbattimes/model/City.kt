package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.location.LocationStatus
import kotlinx.serialization.Serializable
import java.time.ZoneId

@Serializable
data class City(
    val id: String,
    val name: String,
    val coordinates: Coordinates,
    val locationStatus: LocationStatus = LocationStatus.Unknown,
    @Serializable(with = ZoneIdAsStringSerializer::class)
    val timeZone: ZoneId,
    val timeFormat: Int = 24,
)

/**
 * DSL builder for creating City instances
 *
 * Example:
 * ```
 * val jerusalem = city {
 *     name = "Jerusalem"
 *     coordinates = Coordinates(31.7683, 35.2137)
 *     timeZone = ZoneId.of("Asia/Jerusalem")
 * }
 * ```
 */
fun city(block: CityBuilder.() -> Unit): City = CityBuilder().apply(block).build()