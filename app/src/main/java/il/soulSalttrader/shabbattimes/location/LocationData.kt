package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.model.Cities
import il.soulSalttrader.shabbattimes.model.City

data class LocationData(
    val city: City = Cities.JERUSALEM,
)