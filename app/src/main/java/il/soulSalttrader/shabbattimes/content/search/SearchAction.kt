package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.event.SearchEvent.QueryChanged
import il.soulSalttrader.shabbattimes.event.SearchEvent.QueryCleared
import il.soulSalttrader.shabbattimes.event.SearchEvent.SearchCommitted
import il.soulSalttrader.shabbattimes.event.SearchEvent.SearchVisibilityChanged
import il.soulSalttrader.shabbattimes.event.SearchEvent.SuggestionSelected
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.viewModel.SearchViewModel

data class SearchAction(
    val onChangeVisibility: (Boolean) -> Unit,
    val onSearchCommitted: () -> Unit,
    val onSuggestionSelected: (City) -> Unit,
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

