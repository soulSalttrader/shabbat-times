package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.city
import java.time.ZoneId
import java.util.UUID

object SeedCities {
    val JERUSALEM = City(
        id = UUID.randomUUID().toString(),
        name = "Jerusalem",
        coordinates = Coordinates(latitude = 31.77671977237219, longitude = 35.234391805711766),
        timeZone = ZoneId.of("Asia/Jerusalem"),
        timeFormat = 24,
    )

    val NEW_YORK = City(
        id = UUID.randomUUID().toString(),
        name = "New York",
        coordinates = Coordinates(latitude = 40.7128, longitude = -74.0060),
        timeZone = ZoneId.of("America/New_York"),
        timeFormat = 24,
    )

    val BRNO = City(
        id = UUID.randomUUID().toString(),
        name = "Brno",
        coordinates = Coordinates(latitude = 49.194465, longitude = 16.610218),
        timeZone = ZoneId.of("Europe/Vienna"),
        timeFormat = 24,
    )

    val NONE = city {
        name = "Tap to use current location"
        coordinates = Coordinates(latitude = 0.0, longitude = 0.0)
    }

    val initial = listOf(JERUSALEM, NEW_YORK, BRNO)
}