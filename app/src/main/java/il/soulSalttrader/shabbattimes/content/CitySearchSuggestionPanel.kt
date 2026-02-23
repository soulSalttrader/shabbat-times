package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.model.City

private fun LazyListScope.suggestionList(
    suggestions: List<City>,
    onSuggestionSelected: (City) -> Unit,
    modifier: Modifier = Modifier,
) {
    itemsIndexed(
        items = suggestions,
        key = { _, city -> city.id }
    ) { index, suggestion ->
        SuggestionListItem(
            suggestion = suggestion,
            modifier = modifier,
            onSuggestionSelected = onSuggestionSelected,
        )

        if (index < suggestions.lastIndex) {
            HorizontalDivider(
                modifier = Modifier.padding(start = 16.dp)
            )
        }
    }
}

@Composable
private fun SuggestionListItem(
    suggestion: City,
    modifier: Modifier,
    onSuggestionSelected: (City) -> Unit,
) {
    ListItem(
        headlineContent = { Text(suggestion.name) },
        supportingContent = { Text("${suggestion.timeZone}") },
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                onSuggestionSelected(suggestion)
            }
    )
}

private fun LazyListScope.suggestionHint(
    query: String,
    modifier: Modifier = Modifier,
    hint: String = "No results for \"$query\"",
) {
    item {
        Text(
            text = hint,
            modifier = modifier.padding(16.dp),
        )
    }
}