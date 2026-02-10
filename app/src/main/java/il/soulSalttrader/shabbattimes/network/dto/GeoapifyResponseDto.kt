package il.soulSalttrader.shabbattimes.network.dto

import kotlinx.serialization.Serializable

@Serializable
data class GeoapifyResponseDto(
    val results: List<GeoapifyResultDto>? = null,
    val status: String? = null,
    val query: GeoapifyQuery? = null
)