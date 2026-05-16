package il.soulSalttrader.shabbattimes.ui.search

sealed interface SearchVisibility {
    data object Collapsed : SearchVisibility
    data object Expanded : SearchVisibility
}