package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyDatasource(
    @SerialName("sourcename")
    val sourceName: String? = null,
    val attribution: String? = null
)