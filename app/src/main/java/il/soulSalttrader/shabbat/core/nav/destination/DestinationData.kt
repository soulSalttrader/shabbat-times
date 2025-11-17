package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.UiIcon
import il.soulSalttrader.retro.core.nav.NavRole
import il.soulSalttrader.retro.core.nav.NavTarget

data class DestinationData(
    override val target: NavTarget,
    override val title: String,
    override val selectedIcon: UiIcon,
    override val unselectedIcon: UiIcon,
    override val role: NavRole,
) : Destination