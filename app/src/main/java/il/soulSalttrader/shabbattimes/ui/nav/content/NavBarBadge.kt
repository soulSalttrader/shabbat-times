package il.soulSalttrader.shabbattimes.ui.nav.content

import androidx.compose.material3.Badge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import il.soulSalttrader.shabbattimes.R

@Composable
fun NavBarBadge(count: Int? = null) {
    val displayCount = count?.takeIf { it > 0 } ?: return

    Badge(
        containerColor = MaterialTheme.colorScheme.tertiary,
        contentColor = MaterialTheme.colorScheme.onTertiary,
    ) {
        Text(text = if (displayCount > 99) stringResource(R.string.display_count_max) else displayCount.toString())
    }
}