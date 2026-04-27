package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.repository.SeedCities
import kotlinx.serialization.Serializable

@Serializable
data class HalachicTimesDisplay(
    val city: City = SeedCities.NONE,
    val candleLightingTime: String = "--:--",
    val candleLightingDate: String = "",
    val havdalahTime: String = "--:--",
    val havdalahDate: String = "",
)