package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyRank(
    val importance: Double? = null,
    val popularity: Double? = null,
    val confidence: Double? = null,
    @SerialName("confidence_city_level")
    val confidenceCityLevel: Double? = null,
)