package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


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