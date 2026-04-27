package il.soulSalttrader.shabbattimes.location

fun LocationStatus.getLocationLabels() = when (this) {
    is LocationStatus.Current -> "Your current location"
    is LocationStatus.Nearby  -> "(${km.formatWithSpaces()} km away)"
    is LocationStatus.Unknown -> "Unknown distance"
}

private fun Int.formatWithSpaces() = toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()