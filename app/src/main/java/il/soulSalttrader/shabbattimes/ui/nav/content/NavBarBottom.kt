package il.soulSalttrader.shabbattimes.ui.nav.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.ui.nav.NavItem
import il.soulSalttrader.shabbattimes.ui.nav.NavTarget
import il.soulSalttrader.shabbattimes.ui.nav.Navigator
import il.soulSalttrader.shabbattimes.ui.nav.titleOr

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
                        text = item.target.titleOr().asString(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                },
                selected = isSelected,
                onClick = onClick,
            )
        }
    }
}