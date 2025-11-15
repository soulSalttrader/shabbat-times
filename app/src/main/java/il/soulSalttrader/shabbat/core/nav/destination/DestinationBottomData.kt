package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.nav.NavTargetBottom
import il.soulSalttrader.retro.core.UiIcon

data class DestinationBottomData(
    override val target: NavTargetBottom,
    override val title: String,
    override val selectedIcon: UiIcon,
    override val unselectedIcon: UiIcon,
) : DestinationBottom