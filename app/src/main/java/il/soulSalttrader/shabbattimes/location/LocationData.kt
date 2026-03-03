package il.soulSalttrader.shabbattimes.location

import il.soulSalttrader.shabbattimes.repository.SeedCities
import il.soulSalttrader.shabbattimes.model.City

data class LocationData(
    val city: City = SeedCities.JERUSALEM,
)