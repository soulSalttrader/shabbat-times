package il.soulSalttrader.shabbattimes.ui

import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon

data class FabItem(
    val title: UiText?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
    val action: FabAction,
)
