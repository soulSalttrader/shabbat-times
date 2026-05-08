package il.soulSalttrader.shabbattimes.ui

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

fun Selection<ResolvedLocation?>.normalizedOrNull(): ResolvedLocation? =
    when (this) {
        is Selection.Idle            -> null
        is Selection.None                        -> null
        is Selection.Selected<ResolvedLocation?> -> value
    }