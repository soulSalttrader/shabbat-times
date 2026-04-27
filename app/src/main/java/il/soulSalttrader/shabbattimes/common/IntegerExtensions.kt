package il.soulSalttrader.shabbattimes.common

fun Int.formatWithSpaces() = toString()
    .reversed()
    .chunked(3)
    .joinToString(" ")
    .reversed()