package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon

object SearchItems {

    val Add = SearchItem(
        title = UiText.Resource(R.string.search_new_location),
        selectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
        unselectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
    )
}