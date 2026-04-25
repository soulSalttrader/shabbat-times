package il.soulSalttrader.shabbattimes.content.city

import il.soulSalttrader.shabbattimes.common.formatWithSpaces

fun CityStatus.getLocationLabels() = when (this) {
    is CityStatus.Current -> "Your current location"
    is CityStatus.Nearby  -> "(${km.formatWithSpaces()} km away)"
    is CityStatus.Unknown -> "Unknown distance"
}