package il.soulSalttrader.retro.core.nav

import il.soulSalttrader.retro.core.content.component.UiIcon

data class NavItem(
    val target: NavTarget,
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)