package il.soulSalttrader.shabbattimes.content.search

sealed interface SearchMode {
    data object Autocomplete : SearchMode
    data object Forward : SearchMode
    data object Reverse : SearchMode
}