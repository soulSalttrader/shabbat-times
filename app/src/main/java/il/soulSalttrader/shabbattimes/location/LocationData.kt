package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.Cities

data class LocationData(
    val lat: Double = Cities.JERUSALEM.lat,
    val lng: Double = Cities.JERUSALEM.lng,
    val timezone: String = Cities.JERUSALEM.timezone,
)