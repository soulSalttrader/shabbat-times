package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class ShabbatResponse(
    val results: Shabbat = Shabbat(),
    val status: String = "",
)