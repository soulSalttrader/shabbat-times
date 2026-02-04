package il.soulSalttrader.shabbattimes.location

fun LocationStatus.getLocationLabels() = when (this) {
    is LocationStatus.Current  -> "Your current location"
    is LocationStatus.Distance -> "(${km.formatWithSpaces()} km away)"
    is LocationStatus.Unknown -> "Unknown distance"
}

private fun Int.formatWithSpaces() = toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()