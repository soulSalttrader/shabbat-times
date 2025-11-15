package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.nav.route.NavTarget
import il.soulSalttrader.retro.core.UiIcon

data class DestinationTopData(
    override val target: NavTarget,
    override val title: String,
    override val selectedIcon: UiIcon,
    override val unselectedIcon: UiIcon,
    override val role: TopBarRole,
) : DestinationTop