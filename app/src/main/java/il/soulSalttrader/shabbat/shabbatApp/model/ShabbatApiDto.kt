package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class ShabbatApiDto(
    val results: Shabbat = Shabbat(),
    val status: String = "",
)