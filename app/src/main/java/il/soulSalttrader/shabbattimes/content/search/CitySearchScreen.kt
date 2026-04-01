package il.soulSalttrader.shabbattimes.content.search

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
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun CitySearchScreen(
    searchConfig: SearchConfig,
    modifier: Modifier = Modifier,
) {
    val state = rememberTextFieldState("")

    LaunchedEffect(Unit) {
        snapshotFlow { state.text.toString() }
            .distinctUntilChanged()
            .debounce(300)
            .collect { query -> searchConfig.action.onQueryChanged(query) }
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
                hasQuery = searchConfig.state.hasQuery,
                expanded = searchConfig.state.searchActive,
                onExpandedChange = { expanded -> searchConfig.action.onChangeVisibility(!expanded) },
                onSearch = { query ->
                    searchConfig.action.onQueryChanged(query)
                    searchConfig.action.onSearchCommitted()
                },
                onClear = {
                    searchConfig.action.onQueryCleared()
                    state.clearText()
                },
            )

            CitySearchSuggestionPanel(
                query = state.text.toString(),
                expanded = searchConfig.state.searchActive,
                suggestions = searchConfig.state.suggestions,
                onSuggestionSelected = { suggestion ->
                    searchConfig.action.onSuggestionSelected(suggestion)
                    state.setTextAndPlaceCursorAtEnd(suggestion.name)
                },
            )
        }
    }
}