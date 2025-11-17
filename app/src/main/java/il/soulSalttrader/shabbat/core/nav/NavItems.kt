package il.soulSalttrader.retro.core.nav

import il.soulSalttrader.retro.R
import il.soulSalttrader.retro.core.UiIcon

object NavItems {
    val Previous = NavItem(
        target = NavTargetTop.Previous,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(R.drawable.arrow_back_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.arrow_back_outlined_24px),
        role = NavRole.TOP_NAVIGATION,
    )

    val History = NavItem(
        target = NavTargetTop.History,
        title = "History",
        selectedIcon = UiIcon.Resource(resId = R.drawable.overview_filled_24),
        unselectedIcon = UiIcon.Resource(resId = R.drawable.overview_outlined_24),
        role = NavRole.TOP_ACTION,
    )

    val Favorite = NavItem(
        target = NavTargetTop.Favorite,
        title = "Favorite",
        selectedIcon = UiIcon.Resource(R.drawable.favorite_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.favorite_outlined_24),
        role = NavRole.TOP_ACTION,
    )

    val Home = NavItem(
        target = NavTargetBottom.Home,
        title = "Home",
        selectedIcon = UiIcon.Resource(R.drawable.home_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.home_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val Alerts = NavItem(
        target = NavTargetBottom.Alerts,
        title = "Alerts",
        selectedIcon = UiIcon.Resource(R.drawable.notifications_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.notifications_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val Settings = NavItem(
        target = NavTargetBottom.Settings,
        title = "Settings",
        selectedIcon = UiIcon.Resource(R.drawable.settings_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.settings_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val More = NavItem(
        target = NavTargetBottom.More,
        title = "More",
        selectedIcon = UiIcon.Resource(R.drawable.menu_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.menu_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val Breathe = NavItem(
        target = NavTargetBottom.Breathe,
        title = "Breathe",
        selectedIcon = UiIcon.Resource(R.drawable.relax_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.relax_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val Shabbat = NavItem(
        target = NavTargetBottom.Shabbat,
        title = "Shabbat",
        selectedIcon = UiIcon.Resource(R.drawable.candle_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.candle_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val allBottomTabs = listOf(Alerts, Breathe, Home, Settings, Shabbat)
    val allTopTabs = listOf(Previous, Favorite, History)
    val allTopNavigation = listOf(Previous)
    val allTopActions = listOf(Favorite, History)
}