package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.content.Input
import il.soulSalttrader.shabbattimes.content.normalizedOrEmpty
import il.soulSalttrader.shabbattimes.model.City

fun SearchUiState.isSearchActive(): Boolean =
    visibility == SearchVisibility.Expanded

fun SearchUiState.suggestionsOrEmpty(): List<City> =
    (resultState as? SearchResultState.Results)?.cities.orEmpty()

fun SearchUiState.hasQuery(): Boolean =
    query is Input.Value && query.normalizedOrEmpty().isNotEmpty()