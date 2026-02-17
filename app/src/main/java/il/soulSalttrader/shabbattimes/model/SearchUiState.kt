package il.soulSalttrader.shabbattimes.model

data class SearchUiState(
    val query: String = "",
    val selectedSuggestion: City? = null,
    val searchState: SearchState = SearchState.Idle,
    val searchMode: SearchMode = SearchMode.Autocomplete
) : State