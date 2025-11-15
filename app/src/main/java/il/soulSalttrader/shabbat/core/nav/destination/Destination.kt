package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.core.UiIcon
import il.soulSalttrader.retro.core.nav.NavTarget

interface Destination {
    val target: NavTarget
    val title: String
    val selectedIcon: UiIcon
    val unselectedIcon: UiIcon
}