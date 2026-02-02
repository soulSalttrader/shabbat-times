package il.soulSalttrader.shabbattimes.location

fun LocationStatus.getLocationLabels() = when (this) {
    is LocationStatus.Current  -> label
    is LocationStatus.Distance -> "(${km.formatWithSpaces()} km away)"
}

private fun Int.formatWithSpaces() = toString()
        .reversed()
        .chunked(3)
        .joinToString(" ")
        .reversed()