package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.ResolvedLocation

data class SearchState(
    val suggestions: List<ResolvedLocation>,
    val hasQuery: Boolean,
    val searchActive: Boolean,
)