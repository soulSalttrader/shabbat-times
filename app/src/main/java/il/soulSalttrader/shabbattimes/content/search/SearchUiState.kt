package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.State

data class SearchUiState(
    val query: String = "",
    val selectedSuggestion: City? = null,
    val resultState: SearchResultState = SearchResultState.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
    val searchMode: SearchMode = SearchMode.Autocomplete,
) : State