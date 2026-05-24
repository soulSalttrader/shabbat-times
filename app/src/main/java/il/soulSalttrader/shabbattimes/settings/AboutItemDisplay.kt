package il.soulSalttrader.shabbattimes.settings

data class AboutItemDisplay(
    val label: String,
    val value: String,
    val onClick: (() -> Unit)? = null,
)