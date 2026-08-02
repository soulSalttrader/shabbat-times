package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.ui.Input
import il.soulSalttrader.shabbattimes.ui.normalizedOrEmpty

fun SearchUiState.isSearchActive(): Boolean = visibility == SearchVisibility.Expanded

fun SearchUiState.suggestionsOrEmpty(): List<ResolvedLocation> =
    (suggestionResults as? SearchResultState.Suggestions)?.suggestions.orEmpty()

fun SearchUiState.hasQuery(): Boolean = query is Input.Value && query.normalizedOrEmpty().isNotEmpty()

fun SearchUiState.default() = SearchState(
    suggestions = suggestionsOrEmpty(),
    hasQuery = hasQuery(),
    searchActive = isSearchActive(),
)
