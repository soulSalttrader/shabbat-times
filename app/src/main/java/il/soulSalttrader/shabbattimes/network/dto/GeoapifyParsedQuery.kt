package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyParsedQuery(
    @SerialName("housenumber") val houseNumber: String? = null,
    @SerialName("expected_type") val expectedType: String? = null,
    val street: String? = null,
    val postcode: String? = null,
    val city: String? = null,
    val country: String? = null,
)