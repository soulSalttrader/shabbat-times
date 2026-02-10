package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyQuery(
    @SerialName("expected_type") val expectedType: String? = null,
    val text: String? = null,
    val parsed: GeoapifyParsedQuery? = null,
)