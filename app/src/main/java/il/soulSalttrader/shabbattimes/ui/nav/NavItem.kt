package il.soulSalttrader.shabbattimes.ui.nav

import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon

data class NavItem(
    val target: NavTarget,
    val title: UiText?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)