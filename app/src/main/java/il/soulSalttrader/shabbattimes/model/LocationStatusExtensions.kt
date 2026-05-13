package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.common.roundToDecimalPlaces

fun LocationStatus.toLabel() = when (this) {
    is LocationStatus.Current      -> "Your current location"
    is LocationStatus.Nearby       -> "(${distanceKm.roundToDecimalPlaces(1)} km away)"
    is LocationStatus.Locating     -> "Getting your location..."
    is LocationStatus.Unknown      -> "Unknown distance"
    is LocationStatus.NoPermission -> "Tap to use current location"
    is LocationStatus.LastKnownLocation -> "Last known location"
}