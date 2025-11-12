package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.nav.route.RouteBottom
import il.soulSalttrader.retro.core.UiIcon

data class DestinationBottomData(
    override val route: RouteBottom,
    override val title: String,
    override val selectedIcon: UiIcon,
    override val unselectedIcon: UiIcon,
) : DestinationBottom