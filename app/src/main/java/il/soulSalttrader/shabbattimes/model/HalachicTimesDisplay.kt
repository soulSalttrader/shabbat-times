package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Immutable
import kotlinx.serialization.Serializable

@Immutable
@Serializable
data class HalachicTimesDisplay(
    val coordinates: Coordinates,
    val candleLightingTime: String = EMPTY_TIME,
    val candleLightingDate: String = EMPTY_DATE,
    val havdalahTime: String = EMPTY_TIME,
    val havdalahDate: String = EMPTY_DATE,
) {
    companion object {
        const val EMPTY_TIME = "--:--"
        const val EMPTY_DATE = "dd/mm/yyyy"
    }
}