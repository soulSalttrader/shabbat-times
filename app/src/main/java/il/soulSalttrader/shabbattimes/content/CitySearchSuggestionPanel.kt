package il.soulSalttrader.shabbattimes.content

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.model.City

@Composable
fun ColumnScope.CitySearchSuggestionPanel(
    query: String,
    expanded: Boolean,
    suggestions: List<City>,
    onSuggestionSelected: (City) -> Unit,

    modifier: Modifier = Modifier,

    visibilityModifier: Modifier = Modifier,

    containerModifier: Modifier = Modifier,
    containerShape: Shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
    containerTonalElevation: Dp = 4.dp,
    containerMaxHeight: Dp = 320.dp,

    listModifier: Modifier = Modifier,
    itemsContent: LazyListScope.() -> Unit = {},
) {
    AnimatedVisibility(
        modifier = modifier.then(visibilityModifier),
        visible = expanded,
    ) {
        Surface(
            modifier = containerModifier
                .fillMaxWidth()
                .heightIn(max = containerMaxHeight),
            shape = containerShape,
            tonalElevation = containerTonalElevation,
        ) {
            LazyColumn(
                modifier = listModifier.fillMaxWidth(),
            ) {
                itemsContent()

                when {
                    suggestions.isNotEmpty() -> suggestionList(suggestions, onSuggestionSelected)
                    query.isNotEmpty()       -> suggestionHint(query)
                }
            }
        }
    }
}

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