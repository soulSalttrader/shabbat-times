package il.soulSalttrader.shabbattimes.model

sealed interface SearchMode {
    object Autocomplete : SearchMode
    object Forward : SearchMode
    object Reverse : SearchMode
}