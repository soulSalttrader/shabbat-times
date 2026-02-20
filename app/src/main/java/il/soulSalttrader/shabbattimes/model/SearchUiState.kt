package il.soulSalttrader.shabbattimes.model

data class SearchUiState(
    val query: String = "",
    val selectedSuggestion: City? = null,
    val resultState: SearchResultState = SearchResultState.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
    val searchMode: SearchMode = SearchMode.Autocomplete,
) : State