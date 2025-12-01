package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class Shabbat(
    val date: String = "",
    val sunrise: String = "",
    val sunset: String = "",
)