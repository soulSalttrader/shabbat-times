package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.ui.Input
import il.soulSalttrader.shabbattimes.ui.Selection
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.State

data class SearchUiState(
    val query: Input<String> = Input.Idle,
    val suggestionResults: SearchResultState = SearchResultState.Idle,
    val gpsResult: SearchResultState = SearchResultState.Idle,
    val selectedSuggestion: Selection<ResolvedLocation?> = Selection.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
) : State