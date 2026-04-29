package il.soulSalttrader.shabbattimes.location

fun LocationStatus.toLabel() = when (this) {
    is LocationStatus.Current      -> "Your current location"
    is LocationStatus.Nearby       -> "(${distanceKm.formatWithSpaces()} km away)"
    is LocationStatus.Locating     -> "Getting your location..."
    is LocationStatus.Unknown      -> "Unknown distance"
    is LocationStatus.NoPermission -> "Tap to use current location"
}

private fun Double.formatWithSpaces() = toString()
    .reversed()
    .chunked(3)
    .joinToString(" ")
    .reversed()