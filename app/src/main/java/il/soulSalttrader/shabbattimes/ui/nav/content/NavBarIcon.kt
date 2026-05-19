package il.soulSalttrader.shabbattimes.ui.nav.content

import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage
import il.soulSalttrader.shabbattimes.ui.nav.NavItem

@Composable
fun NavBarIcon(
    isSelected: Boolean,
    item: NavItem,
    badgeCount: Int? = null,
) {
    BadgedBox(badge = { NavBarBadge(badgeCount) }) {
        UiIconImage(
            icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title?.asString(),
        )
    }
}