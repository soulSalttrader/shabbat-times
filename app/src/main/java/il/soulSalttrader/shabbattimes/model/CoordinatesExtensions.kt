package il.soulSalttrader.shabbattimes.model

import android.location.Location
import il.soulSalttrader.shabbattimes.common.roundToDecimalPlaces
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun Coordinates.normalize() = Coordinates(
    latitude = latitude.roundToDecimalPlaces(),
    longitude = longitude.roundToDecimalPlaces(),
)

fun Location.normalize() = Coordinates(
    latitude = latitude.roundToDecimalPlaces(),
    longitude = longitude.roundToDecimalPlaces(),
)

fun Coordinates.distanceTo(other: Coordinates): Double {
    val earthRadiusKm = 6371.0

    val lat1 = Math.toRadians(latitude)
    val lat2 = Math.toRadians(other.latitude)
    val dLat = Math.toRadians(other.latitude - latitude)
    val dLon = Math.toRadians(other.longitude - longitude)

    val a = sin(dLat / 2).pow(2) +
            cos(lat1) * cos(lat2) *
            sin(dLon / 2).pow(2)

    val c = 2 * atan2(sqrt(a), sqrt(1 - a))

    return earthRadiusKm * c
}