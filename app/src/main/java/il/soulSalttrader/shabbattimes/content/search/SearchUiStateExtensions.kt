package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.model.City

fun SearchUiState.isSearchActive(): Boolean =
    visibility == SearchVisibility.Expanded

fun SearchUiState.suggestionsOrEmpty(): List<City> =
    (resultState as? SearchResultState.Results)?.cities.orEmpty()