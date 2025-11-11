package il.soulSalttrader.retro.core.nav.destination

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
        selectedIcon = UiIcon.Resource(R.drawable.arrow_back_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.arrow_back_outlined_24px),
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
        selectedIcon = UiIcon.Resource(R.drawable.favorite_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.favorite_outlined_24),
        badgeCount = null,
        role = TopBarRole.ACTION,
    )
}