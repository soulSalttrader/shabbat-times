package il.soulSalttrader.shabbattimes.content.search

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
import il.soulSalttrader.shabbattimes.model.ResolvedLocation

@Composable
fun ColumnScope.LocationSearchSuggestionPanel(
    query: String,
    expanded: Boolean,
    suggestions: List<ResolvedLocation>,
    onSuggestionSelected: (ResolvedLocation) -> Unit,

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
    suggestions: List<ResolvedLocation>,
    onSuggestionSelected: (ResolvedLocation) -> Unit,
    modifier: Modifier = Modifier,
) {
    itemsIndexed(
        items = suggestions,
        key = { index, _ -> index }
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
    suggestion: ResolvedLocation,
    modifier: Modifier,
    onSuggestionSelected: (ResolvedLocation) -> Unit,
) {
    ListItem(
        headlineContent = { Text(suggestion.name) },
        supportingContent = { Text("${suggestion.timeZoneId}") },
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