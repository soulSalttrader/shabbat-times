package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
import androidx.compose.material3.FloatingActionButtonMenuScope
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.model.SearchItem
import il.soulSalttrader.shabbattimes.model.SearchItems.Add

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    items: List<SearchItem> = listOf(Add),
) {
    var internalExpanded by remember { mutableStateOf(false) }
    val isExpanded = expanded ?: internalExpanded

    val toggleExpanded = {
        val newValue = !isExpanded
        onExpandedChange?.invoke(newValue)
        if (expanded == null) internalExpanded = newValue
    }

    FloatingActionButtonMenu(
        modifier = modifier,
        expanded = isExpanded,
        button = { FabMenuButton(isExpanded, toggleExpanded) },
    ) {
        FabMenuItems(items, onClick, toggleExpanded)
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun FloatingActionButtonMenuScope.FabMenuItems(
    items: List<SearchItem>,
    onClick: () -> Unit,
    toggleExpanded: () -> Unit,
) {
    items.forEach { item ->
        FloatingActionButtonMenuItem(
            onClick = {
                onClick()
                toggleExpanded()
            },
            text = { item.title?.let { Text(it) } },
            icon = { UiIconImage(icon = item.unselectedIcon, contentDescription = item.title) }
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun FabMenuButton(
    isExpanded: Boolean,
    toggleExpanded: () -> Unit,
    expandedIconRes: Int = R.drawable.close_outlined_24,
    collapsedIconRes: Int = R.drawable.add_outlined_24,
    contentDescription: String? = null,
) {
    ToggleFloatingActionButton(
        checked = isExpanded,
        onCheckedChange = { toggleExpanded() },
    ) {
        UiIconImage(
            icon = UiIcon.Resource(
                resId = isExpanded.takeIf { it }
                    ?.let { expandedIconRes }
                    ?: collapsedIconRes
            ),
            contentDescription = contentDescription,
            contentColor = isExpanded.takeIf { it }
                ?.let { MaterialTheme.colorScheme.onPrimary }
                ?: MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}