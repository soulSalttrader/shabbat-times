package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class Coordinates(
    val latitude: Double,
    val longitude: Double,
) {
    init {
        require(latitude in -90.0..90.0) { "Invalid latitude: $latitude" }
        require(longitude in -180.0..180.0) { "Invalid longitude: $longitude" }
    }

    companion object {
        val EMPTY = Coordinates(0.0, 0.0)
    }
}