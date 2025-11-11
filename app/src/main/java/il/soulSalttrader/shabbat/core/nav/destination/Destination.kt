package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.nav.route.Route
import il.soulSalttrader.retro.core.UiIcon

interface Destination {
    val route: Route
    val title: String
    val selectedIcon: UiIcon
    val unselectedIcon: UiIcon
    val badgeCount: Int?
}