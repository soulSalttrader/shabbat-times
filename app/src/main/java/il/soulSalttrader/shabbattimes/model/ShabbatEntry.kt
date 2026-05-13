package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class ShabbatEntry(
    val location: SavedLocation,
    val times: HalachicTimesDisplay?,
    val status: LocationStatus,
)