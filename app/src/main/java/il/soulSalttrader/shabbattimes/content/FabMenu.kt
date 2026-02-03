package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FloatingActionButtonMenu
import androidx.compose.material3.FloatingActionButtonMenuItem
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
import il.soulSalttrader.shabbattimes.nav.NavItem
import il.soulSalttrader.shabbattimes.nav.Navigator
import kotlin.collections.forEach

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun FabMenu(
    navItems: List<NavItem>,
    navigator: Navigator,
    modifier: Modifier = Modifier,
    expanded: Boolean? = null,
    onExpandedChange: ((Boolean) -> Unit)? = null,
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
        button = {
            ToggleFloatingActionButton(
                checked = isExpanded,
                onCheckedChange = { toggleExpanded() },
            ) {
                UiIconImage(
                    icon = UiIcon.Resource(
                        isExpanded.takeIf { it }
                            ?.let { R.drawable.close_outlined_24 } ?: R.drawable.add_outlined_24
                    ),
                    contentDescription = null,
                    contentColor = isExpanded.takeIf { it }
                        ?.let { MaterialTheme.colorScheme.onPrimary }
                        ?: MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        },
    ) {
        navItems.forEach { item ->
            FloatingActionButtonMenuItem(
                onClick = { navigator.navigateTo(item.target) },
                text = { item.title?.let { Text(it) } },
                icon = { UiIconImage(icon = item.unselectedIcon, contentDescription = null) }
            )
        }
    }
}