package il.soulSalttrader.retro.core.nav.destination

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import kotlinx.serialization.Serializable
import il.soulSalttrader.retro.R
import il.soulSalttrader.retro.core.nav.route.RouteBottom
import il.soulSalttrader.retro.core.UiIcon

@Serializable
sealed interface DestinationNavBottom : DestinationBottom {

    @Serializable
    data object Home : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        route = RouteBottom.HomeScreen,
        title = "Home",
        selectedIcon = UiIcon.Vector(Icons.Filled.Home),
        unselectedIcon = UiIcon.Vector(Icons.Outlined.Home),
    )

    @Serializable
    data object Alerts : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        route = RouteBottom.AlertsScreen,
        title = "Alerts",
        selectedIcon = UiIcon.Vector(Icons.Filled.Notifications),
        unselectedIcon = UiIcon.Vector(Icons.Outlined.Notifications),
        badgeCount = 7,
    )

    @Serializable
    data object Settings : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        route = RouteBottom.SettingsScreen,
        title = "Settings",
        selectedIcon = UiIcon.Vector(Icons.Filled.Settings),
        unselectedIcon = UiIcon.Vector(Icons.Outlined.Settings),
    )

//    @Serializable
//    data object More : DestinationNavBottom, DestinationBottom by DestinationBottomData(
//        route = RouteBottom.MoreScreen,
//        title = "More",
//        selectedIcon = UiIcon.Vector(Icons.Filled.Menu),
//        unselectedIcon = UiIcon.Vector(Icons.Outlined.Menu),
//    )

    @Serializable
    data object Breathe : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        route = RouteBottom.BreatheScreen,
        title = "Breath",
        selectedIcon = UiIcon.Resource(R.drawable.relax_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.relax_outlined_24),
    )

    @Serializable
    data object Shabbat : DestinationNavBottom, DestinationBottom by DestinationBottomData(
        route = RouteBottom.ShabbatScreen,
        title = "Shabbat",
        selectedIcon = UiIcon.Resource(R.drawable.candle_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.candle_outlined_24),
    )
}