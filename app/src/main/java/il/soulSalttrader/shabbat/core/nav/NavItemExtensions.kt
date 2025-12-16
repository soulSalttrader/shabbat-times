package il.soulSalttrader.retro.core.nav

fun List<NavItem>.extractTopBarItems(): Pair<NavItem?, List<NavItem>> {
    val topNavigationItem = this.find { it.role == NavRole.TOP_NAVIGATION }
    val topActionItems = this.filter { it.role == NavRole.TOP_ACTION }
    return topNavigationItem to topActionItems
}