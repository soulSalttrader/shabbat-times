package il.soulSalttrader.shabbattimes.nav

data class NavItem(
    val target: NavTarget,
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val role: NavRole,
)