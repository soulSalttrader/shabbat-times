package il.soulSalttrader.shabbattimes.model

import kotlinx.serialization.Serializable

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