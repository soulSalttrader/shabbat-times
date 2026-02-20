package il.soulSalttrader.shabbattimes.model

sealed interface SearchVisibility {
    object Collapsed : SearchVisibility
    object Expanded : SearchVisibility
}