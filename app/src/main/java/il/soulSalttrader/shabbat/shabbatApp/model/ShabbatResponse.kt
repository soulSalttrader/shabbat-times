package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class ShabbatResponse(
    val results: ShabbatResult = ShabbatResult(),
    val status: String = "",
)