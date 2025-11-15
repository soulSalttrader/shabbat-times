package il.soulSalttrader.retro.core.nav.destination

import kotlinx.serialization.Serializable
import il.soulSalttrader.retro.R
import il.soulSalttrader.retro.core.nav.route.NavTargetBottom
import il.soulSalttrader.retro.core.UiIcon

@Serializable
sealed interface DestinationNavBottom : DestinationBottom {

    @Serializable
    data object Home : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        target = NavTargetBottom.Home,
        title = "Home",
        selectedIcon = UiIcon.Resource(R.drawable.home_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.home_outlined_24),
    )

    @Serializable
    data object Alerts : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        target = NavTargetBottom.Alerts,
        title = "Alerts",
        selectedIcon = UiIcon.Resource(R.drawable.notifications_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.notifications_outlined_24),
    )

    @Serializable
    data object Settings : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        target = NavTargetBottom.Settings,
        title = "Settings",
        selectedIcon = UiIcon.Resource(R.drawable.settings_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.settings_outlined_24),
    )

//    @Serializable
//    data object More : DestinationNavBottom, DestinationBottom by DestinationBottomData(
//        target = NavTargetBottom.More,
//        title = "More",
//        selectedIcon = UiIcon.Resource(R.drawable.menu_filled_24),
//        unselectedIcon = UiIcon.Resource(R.drawable.menu_outlined_24),
//    )

    @Serializable
    data object Breathe : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        target = NavTargetBottom.Breathe,
        title = "Breath",
        selectedIcon = UiIcon.Resource(R.drawable.relax_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.relax_outlined_24),
    )

    @Serializable
    data object Shabbat : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        target = NavTargetBottom.Shabbat,
        title = "Shabbat",
        selectedIcon = UiIcon.Resource(R.drawable.candle_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.candle_outlined_24),
    )
}