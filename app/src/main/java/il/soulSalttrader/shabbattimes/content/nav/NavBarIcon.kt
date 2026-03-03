package il.soulSalttrader.shabbattimes.content.nav

import androidx.compose.material3.BadgedBox
import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIconImage
import il.soulSalttrader.shabbattimes.nav.NavItem

@Composable
fun NavBarIcon(
    isSelected: Boolean,
    item: NavItem,
    badgeCount: Int? = null,
) {
    BadgedBox(badge = { NavBarBadge(badgeCount) }) {
        UiIconImage(
            icon = if (isSelected) item.selectedIcon else item.unselectedIcon,
            contentDescription = item.title,
        )
    }
}