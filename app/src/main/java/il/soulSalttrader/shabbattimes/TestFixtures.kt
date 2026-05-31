package il.soulSalttrader.shabbattimes

import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import java.time.ZoneId

fun jerusalemLocation(id: String = "id_1") = SavedLocation(
    id = id,
    name = "Jerusalem",
    coordinates = Coordinates(31.7683, 35.2137),
    timeZoneId = ZoneId.of("Asia/Jerusalem"),
)

fun telAvivLocation(id: String = "id_2") = SavedLocation(
    id = id,
    name = "Tel Aviv",
    coordinates = Coordinates(32.0853, 34.7818),
    timeZoneId = ZoneId.of("Asia/Tel_Aviv"),
)

fun brnoLocation() = SavedLocation(
    id = "id_3",
    name = "Brno",
    coordinates = Coordinates(49.1951, 16.6068),
    timeZoneId = ZoneId.of("Europe/Prague"),
)

fun gpsLocation() = SavedLocation(
    id = SavedLocation.GPS_ID,
    name = "Current Location",
    coordinates = Coordinates(0.0, 0.0),
    timeZoneId = ZoneId.systemDefault(),
)

val jerusalemEntry = ShabbatEntry(
    location = jerusalemLocation(),
    times = HalachicTimesDisplay(jerusalemLocation().coordinates),
    status = LocationStatus.Nearby(1.0),
)

val telAvivEntry = ShabbatEntry(
    location = telAvivLocation(),
    times = HalachicTimesDisplay(telAvivLocation().coordinates),
    status = LocationStatus.Nearby(2.0),
)

val brnoEntry = ShabbatEntry(
    location = brnoLocation(),
    times = HalachicTimesDisplay(brnoLocation().coordinates),
    status = LocationStatus.Nearby(3.0),
)

val gpsEntry = ShabbatEntry(
    location = gpsLocation(),
    times = HalachicTimesDisplay(gpsLocation().coordinates),
    status = LocationStatus.Current,
)