package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.content.Input
import il.soulSalttrader.shabbattimes.content.Selection
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.State

data class SearchUiState(
    val query: Input<String> = Input.Idle,
    val suggestionResults: SearchResultState = SearchResultState.Idle,
    val gpsResult: SearchResultState = SearchResultState.Idle,
    val selectedSuggestion: Selection<ResolvedLocation?> = Selection.Idle,
    val visibility: SearchVisibility = SearchVisibility.Collapsed,
) : State