package il.soulSalttrader.retro.shabbatApp.location

import il.soulSalttrader.retro.shabbatApp.model.Cities

data class LocationData(
    val lat: Double = Cities.JERUSALEM.lat,
    val lng: Double = Cities.JERUSALEM.lng,
    val timezone: String = Cities.JERUSALEM.timezone,
)