package il.soulSalttrader.shabbattimes.model

import android.location.Location
import java.math.RoundingMode

private fun Double.roundToDecimalPlaces(
    places: Int = 4,
    mode: RoundingMode = RoundingMode.HALF_UP,
): Double = toBigDecimal().setScale(places, mode).toDouble()

fun Coordinates.normalize() = Coordinates(
    latitude = latitude.roundToDecimalPlaces(),
    longitude = longitude.roundToDecimalPlaces(),
)

fun Location.normalize() = Coordinates(
    latitude = latitude.roundToDecimalPlaces(),
    longitude = longitude.roundToDecimalPlaces(),
)