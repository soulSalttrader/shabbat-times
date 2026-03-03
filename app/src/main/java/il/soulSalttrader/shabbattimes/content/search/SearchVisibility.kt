package il.soulSalttrader.shabbattimes.content.search

sealed interface SearchVisibility {
    object Collapsed : SearchVisibility
    object Expanded : SearchVisibility
}