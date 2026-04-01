package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.City

data class SearchState(
    val suggestions: List<City>,
    val hasQuery: Boolean,
    val searchActive: Boolean,
)

fun SearchUiState.default() = SearchState(
    suggestions = suggestionsOrEmpty(),
    hasQuery = hasQuery(),
    searchActive = isSearchActive(),
)