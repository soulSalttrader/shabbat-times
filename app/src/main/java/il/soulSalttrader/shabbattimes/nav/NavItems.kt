package il.soulSalttrader.shabbattimes.nav

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.UiIcon

object NavItems {
    val Previous = NavItem(
        target = NavTargetTop.Previous,
        title = "Go Back",
        selectedIcon = UiIcon.Resource(R.drawable.arrow_back_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.arrow_back_outlined_24px),
        role = NavRole.TOP_NAVIGATION,
    )

    val Settings = NavItem(
        target = NavTargetTop.Settings,
        title = "Settings",
        selectedIcon = UiIcon.Resource(R.drawable.settings_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.settings_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val Shabbat = NavItem(
        target = NavTargetBottom.Shabbat,
        title = "Shabbat",
        selectedIcon = UiIcon.Resource(R.drawable.candle_filled_24),
        unselectedIcon = UiIcon.Resource(R.drawable.candle_outlined_24),
        role = NavRole.BOTTOM_TAB,
    )

    val allTabs = listOf(Previous, Settings, Shabbat)
}