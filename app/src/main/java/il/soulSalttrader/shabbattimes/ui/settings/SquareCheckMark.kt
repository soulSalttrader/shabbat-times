package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage

@Composable
fun SquareCheckmark(
    selected: Boolean,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(24.dp)
            .clip(RoundedCornerShape(4.dp))
            .background(
                when (selected) {
                    true -> MaterialTheme.colorScheme.primaryContainer
                    false -> MaterialTheme.colorScheme.surfaceVariant
                }
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            UiIconImage(
                icon = UiIcon.Resource(R.drawable.check_small_24),
                contentDescription = "squareCheckMark",
                contentColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}