package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.nav.NavItem
import il.soulSalttrader.shabbattimes.nav.NavTarget
import il.soulSalttrader.shabbattimes.nav.Navigator
import il.soulSalttrader.shabbattimes.nav.common.simpleName

@Composable
fun NavBarBottom(
    navItems: List<NavItem>,
    navigator: Navigator,
    currentNavTarget: NavTarget? = null,
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        NavBarBottomItems(
            navItems = navItems,
            navigator = navigator,
            currentNavTarget = currentNavTarget,
        ) { item, isSelected, onClick ->

            NavigationBarItem(
                icon = {
                    NavBarIcon(
                        isSelected = isSelected,
                        badgeCount = null,
                        item = item,
                    )
                },
                label = {
                    Text(
                        text = item.target.simpleName(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                selected = isSelected,
                onClick = onClick,
            )
        }
    }
}