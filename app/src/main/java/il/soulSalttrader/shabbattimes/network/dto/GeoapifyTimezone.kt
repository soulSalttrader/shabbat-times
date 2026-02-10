package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyTimezone(
    val name: String? = null,
    @SerialName("offset_STD") val offsetStandard: String? = null,
    @SerialName("abbreviation_STD") val abbreviationStandard: String? = null,
)