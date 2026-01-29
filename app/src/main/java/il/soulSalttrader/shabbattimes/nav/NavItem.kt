package il.soulSalttrader.shabbattimes.nav

import il.soulSalttrader.shabbattimes.content.UiIcon

data class NavItem(
    val target: NavTarget,
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)