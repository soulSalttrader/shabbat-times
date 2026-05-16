package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.ui.event.SearchEvent.QueryChanged
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent.QueryCleared
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent.SearchCommitted
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent.SearchVisibilityChanged
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent.SuggestionSelected
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.ui.viewModel.SearchViewModel

data class SearchAction(
    val onChangeVisibility: (Boolean) -> Unit,
    val onSearchCommitted: () -> Unit,
    val onSuggestionSelected: (ResolvedLocation) -> Unit,
    val onQueryChanged: (String) -> Unit,
    val onQueryCleared: () -> Unit,
)

fun SearchViewModel.default() = SearchAction(
    onChangeVisibility = { visible -> dispatch(SearchVisibilityChanged(visible)) },
    onSearchCommitted = { dispatch(SearchCommitted) },
    onSuggestionSelected = { suggestion -> dispatch(SuggestionSelected(suggestion)) },
    onQueryChanged = { query -> dispatch(QueryChanged(query)) },
    onQueryCleared = { dispatch(QueryCleared) },
)

