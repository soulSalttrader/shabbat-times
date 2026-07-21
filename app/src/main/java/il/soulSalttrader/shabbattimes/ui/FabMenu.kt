package il.soulSalttrader.shabbattimes.ui

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
import androidx.compose.ui.platform.testTag
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.TestTags
import il.soulSalttrader.shabbattimes.ui.FabItems.Search
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    onAction: (FabAction) -> Unit,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
    items: List<FabItem> = listOf(Search),
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
        FabMenuItems(items, onAction, toggleExpanded)
    }
}

@Composable
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
private fun FloatingActionButtonMenuScope.FabMenuItems(
    items: List<FabItem>,
    onAction: (FabAction) -> Unit,
    toggleExpanded: () -> Unit,
) {
    items.forEach { item ->
        FloatingActionButtonMenuItem(
            modifier = when (item) {
                Search -> Modifier.testTag(TestTags.FAB_NEW_LOCATION)
                else   -> Modifier
            },
            onClick = {
                onAction(item.action)
                toggleExpanded()
            },
            text = { item.title?.let { Text(it.asString()) } },
            icon = { UiIconImage(icon = item.unselectedIcon, contentDescription = item.title?.asString()) }
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
        modifier = Modifier.testTag(TestTags.FAB_ADD),
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