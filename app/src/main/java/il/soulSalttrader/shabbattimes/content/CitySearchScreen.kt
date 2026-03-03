package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.model.City
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun CitySearchScreen(
    hasQuery: Boolean,
    suggestions: List<City>,
    searchDispatch: (AppEvent) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val state = rememberTextFieldState("")

    LaunchedEffect(Unit) {
        snapshotFlow { state.text.toString() }
            .distinctUntilChanged()
            .debounce(300)
            .collect { query ->
                searchDispatch(
                    SearchEvent.QueryChanged(newQuery = state.text.toString())
                )
            }
    }

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            CitySearchBarInputField(
                state = state,
                hasQuery = hasQuery,
                expanded = expanded,
                onExpandedChange = { expanded ->
                    searchDispatch(
                        SearchEvent.SearchVisibilityChanged(expanded = !expanded)
                    )
                },
                onSearch = { query ->
                    searchDispatch(SearchEvent.QueryChanged(newQuery = query))
                    searchDispatch(SearchEvent.SearchCommitted)
                },
                onClear = {
                    searchDispatch(SearchEvent.QueryCleared)
                    state.clearText()
                },
            )

            CitySearchSuggestionPanel(
                query = state.text.toString(),
                expanded = expanded,
                suggestions = suggestions,
                onSuggestionSelected = { suggestion ->
                    searchDispatch(SearchEvent.SuggestionSelected(suggestion))
                    state.setTextAndPlaceCursorAtEnd(suggestion.name)
                },
            )
        }
    }
}