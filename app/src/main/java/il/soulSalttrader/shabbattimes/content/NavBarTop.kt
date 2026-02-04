package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import il.soulSalttrader.shabbattimes.nav.NavItem
import il.soulSalttrader.shabbattimes.nav.NavTarget
import il.soulSalttrader.shabbattimes.nav.NavTargetBottom
import il.soulSalttrader.shabbattimes.nav.Navigator
import il.soulSalttrader.shabbattimes.nav.common.extractTopBarItems
import il.soulSalttrader.shabbattimes.nav.common.simpleName

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavBarTop(
    navItems: List<NavItem>,
    navigator: Navigator,
    scrollBehavior: TopAppBarScrollBehavior,
    currentNavTarget: NavTarget? = null,
    isNavIconVisible: Boolean = currentNavTarget != NavTargetBottom.Shabbat,
) {
    val (topNavigationItem, topActionItems) = navItems.extractTopBarItems()

    TopAppBar(
        colors = TopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer,
            scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            titleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            subtitleContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        title = { Text(text = currentNavTarget?.simpleName() ?: "None") },
        navigationIcon = {
            topNavigationItem?.let {
                IconButton(
                    onClick = { navigator.navigateUp() },
                    enabled = isNavIconVisible,
                    modifier = Modifier.alpha(if (isNavIconVisible) 1f else 0f)
                ) {
                    NavBarIcon(
                        isSelected = false,
                        badgeCount = null,
                        item = it,
                    )
                }
            }
        },
        actions = {
            topActionItems.forEach { item ->
                val onItemClick = { navigator.navigateTo(item.target) }

                IconButton(onClick = { onItemClick() }) {
                    NavBarIcon(
                        isSelected = currentNavTarget == item.target,
                        badgeCount = null,
                        item = item,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}