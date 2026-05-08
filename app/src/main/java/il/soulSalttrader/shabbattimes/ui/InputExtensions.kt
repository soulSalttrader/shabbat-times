package il.soulSalttrader.shabbattimes.ui

fun Input<String>.normalizedOrEmpty(): String =
    when (this) {
        is Input.Idle,
        is Input.Empty -> ""
        is Input.Value -> value.trim()
    }