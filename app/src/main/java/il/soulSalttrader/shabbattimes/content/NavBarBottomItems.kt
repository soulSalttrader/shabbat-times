package il.soulSalttrader.shabbattimes.content

import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.nav.NavItem
import il.soulSalttrader.shabbattimes.nav.NavTarget
import il.soulSalttrader.shabbattimes.nav.Navigator

@Composable
fun NavBarBottomItems(
    navItems: List<NavItem>,
    navigator: Navigator,
    currentNavTarget: NavTarget? = null,
    itemContent: @Composable (item: NavItem, isSelected: Boolean, onClick: () -> Unit) -> Unit,
) {
    navItems.forEach { navItem ->
        val isSelected = currentNavTarget == navItem.target
        val onClick = { navigator.navigateTo(target = navItem.target); Unit }

        itemContent(navItem, isSelected, onClick)
    }
}