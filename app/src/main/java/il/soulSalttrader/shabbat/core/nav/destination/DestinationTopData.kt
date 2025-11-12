package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.nav.route.Route
import il.soulSalttrader.retro.core.UiIcon

data class DestinationTopData(
    override val route: Route,
    override val title: String,
    override val selectedIcon: UiIcon,
    override val unselectedIcon: UiIcon,
    override val role: TopBarRole,
) : DestinationTop