package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.model.SearchUiState
import kotlinx.coroutines.FlowPreview

@OptIn(ExperimentalMaterial3Api::class, FlowPreview::class)
@Composable
fun CitySearchScreen(
    searchUiState: SearchUiState,
    searchDispatch: (AppEvent) -> Unit,
    expanded: Boolean,
    modifier: Modifier = Modifier,
) {
    val state = rememberTextFieldState("")

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
                hasQuery = searchUiState.query.isNotEmpty(),
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
                    state.edit { replace(0, length, "") }
                },
            )

        }
    }
}