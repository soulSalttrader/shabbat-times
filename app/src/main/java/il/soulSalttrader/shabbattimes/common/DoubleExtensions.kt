package il.soulSalttrader.shabbattimes.common

import java.math.RoundingMode

fun Double.roundToDecimalPlaces(
    places: Int = 4,
    mode: RoundingMode = RoundingMode.HALF_UP,
): Double = toBigDecimal().setScale(places, mode).toDouble()