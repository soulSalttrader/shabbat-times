package il.soulSalttrader.shabbattimes.content.search

sealed interface SearchVisibility {
    data object Collapsed : SearchVisibility
    data object Expanded : SearchVisibility
}