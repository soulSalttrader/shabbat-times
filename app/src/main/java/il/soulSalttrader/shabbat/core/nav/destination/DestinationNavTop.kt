package il.soulSalttrader.retro.core.nav.destination

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import il.soulSalttrader.retro.R
import il.soulSalttrader.retro.core.UiIcon
import il.soulSalttrader.retro.core.nav.route.RouteTop
import kotlinx.serialization.Serializable

@Serializable
sealed interface DestinationNavTop : DestinationTop {

    @Serializable
    object Previous : DestinationNavTop, DestinationTop by DestinationTopData(
        route = RouteTop.PreviousScreen,
        title = "Go Back",
        selectedIcon = UiIcon.Vector(Icons.AutoMirrored.Filled.KeyboardArrowLeft),
        unselectedIcon = UiIcon.Vector(Icons.AutoMirrored.Outlined.KeyboardArrowLeft),
        badgeCount = null,
        role = TopBarRole.NAVIGATION,
    )

    @Serializable
    object History : DestinationNavTop, DestinationTop by DestinationTopData(
        route = RouteTop.HistoryScreen,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(resId = R.drawable.overview_filled_24),
        unselectedIcon = UiIcon.Resource(resId = R.drawable.overview_outlined_24),
        badgeCount = null,
        role = TopBarRole.ACTION,
    )

    @Serializable
    object Favorite : DestinationNavTop, DestinationTop by DestinationTopData(
        route = RouteTop.FavoriteScreen,
        title = "Go Back",
        selectedIcon = UiIcon.Vector(Icons.Default.Favorite),
        unselectedIcon = UiIcon.Vector(Icons.Default.FavoriteBorder),
        badgeCount = null,
        role = TopBarRole.ACTION,
    )
}