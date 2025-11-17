package il.soulSalttrader.retro.core.nav.destination

import il.soulSalttrader.retro.R
import il.soulSalttrader.retro.core.UiIcon
import il.soulSalttrader.retro.core.nav.NavRole
import il.soulSalttrader.retro.core.nav.NavTargetTop
import kotlinx.serialization.Serializable

@Serializable
sealed interface DestinationNavTop : Destination {

    @Serializable
    object Previous : DestinationNavTop, Destination by DestinationTopData(
        target = NavTargetTop.Previous,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(R.drawable.arrow_back_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.arrow_back_outlined_24px),
        role = NavRole.TOP_NAVIGATION,
    )

    @Serializable
    object History : DestinationNavTop, Destination by DestinationTopData(
        target = NavTargetTop.History,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(resId = R.drawable.overview_filled_24),
        unselectedIcon = UiIcon.Resource(resId = R.drawable.overview_outlined_24),
        role = NavRole.TOP_ACTION,
    )

    @Serializable
    object Favorite : DestinationNavTop, Destination by DestinationTopData(
        target = NavTargetTop.Favorite,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(R.drawable.favorite_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.favorite_outlined_24),
        role = NavRole.TOP_ACTION,
    )
}