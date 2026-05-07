package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

data class SearchState(
    val suggestions: List<ResolvedLocation>,
    val hasQuery: Boolean,
    val searchActive: Boolean,
)

fun SearchUiState.default() = SearchState(
    suggestions = suggestionsOrEmpty(),
    hasQuery = hasQuery(),
    searchActive = isSearchActive(),
)