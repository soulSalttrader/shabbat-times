package il.soulSalttrader.retro.shabbatApp.model

import kotlinx.serialization.Serializable

@Serializable
data class City(
    val name: String,
    val lat: Double,
    val lng: Double,
    val timezone: String,
)