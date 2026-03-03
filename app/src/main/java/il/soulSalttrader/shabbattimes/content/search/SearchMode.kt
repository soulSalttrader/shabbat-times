package il.soulSalttrader.shabbattimes.content.search

sealed interface SearchMode {
    object Autocomplete : SearchMode
    object Forward : SearchMode
    object Reverse : SearchMode
}