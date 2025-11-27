package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class ShabbatResponse(
    val result: ShabbatResult,
    val response: String,
)