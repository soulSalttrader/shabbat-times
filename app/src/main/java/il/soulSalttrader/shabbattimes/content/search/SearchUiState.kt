package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.content.Input
import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.State

data class SearchUiState(
    val query: Input<String> = Input.Idle,
    val selectedSuggestion: Selection<City?> = Selection.Idle,
    val resultState: SearchResultState = SearchResultState.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
    val searchMode: SearchMode = SearchMode.Autocomplete,
) : State