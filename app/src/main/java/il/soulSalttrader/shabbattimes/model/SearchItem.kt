package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.content.UiIcon

data class SearchItem(
    val title: String?,
    val selectedIcon: UiIcon,
    val unselectedIcon: UiIcon,
)