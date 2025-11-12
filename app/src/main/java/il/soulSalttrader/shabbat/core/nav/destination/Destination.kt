package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.UiIcon
import il.soulSalttrader.retro.core.nav.route.Route

interface Destination {
    val route: Route
    val title: String
    val selectedIcon: UiIcon
    val unselectedIcon: UiIcon
}