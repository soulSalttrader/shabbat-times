package il.soulSalttrader.retro.core.nav.route

import il.soulSalttrader.retro.core.UiIcon

data class NavItem(
    val target: NavTarget,
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)