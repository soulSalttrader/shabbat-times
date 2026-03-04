package il.soulSalttrader.shabbattimes.content

import il.soulSalttrader.shabbattimes.model.City

fun Selection<City?>.normalizedOrNull(): City? =
    when (this) {
        is Selection.Idle            -> null
        is Selection.None            -> null
        is Selection.Selected<City?> -> value
    }