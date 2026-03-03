package il.soulSalttrader.shabbattimes.content

import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.SearchResultState
import il.soulSalttrader.shabbattimes.model.SearchUiState
import il.soulSalttrader.shabbattimes.model.SearchVisibility

fun SearchUiState.isSearchActive(): Boolean =
    visibility == SearchVisibility.Expanded

fun SearchUiState.suggestionsOrEmpty(): List<City> =
    (resultState as? SearchResultState.Results)?.cities.orEmpty()