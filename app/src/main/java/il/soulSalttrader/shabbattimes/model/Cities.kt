package il.soulSalttrader.shabbattimes.model

import java.time.ZoneId

object Cities {
    val JERUSALEM = City(
        id = "jerusalem-il",
        name = "Jerusalem",
        coordinates = Coordinates(latitude = 31.768318, longitude = 35.213711),
        timeZone = ZoneId.of("Asia/Jerusalem"),
        candleLightingOffsetMinutes = 20L,
    )

    val NEW_YORK = City(
        id = "newyork-nyc",
        name = "New York",
        coordinates = Coordinates(latitude = 40.7128, longitude = -74.0060),
        timeZone = ZoneId.of("America/New_York"),
        candleLightingOffsetMinutes = 20L,
    )

    val BRNO = City(
        id = "brno-bohemia",
        name = "Brno",
        coordinates = Coordinates(latitude = 49.194465, longitude = 16.610218),
        timeZone = ZoneId.of("Europe/Vienna"),
        candleLightingOffsetMinutes = 18L,
    )
}