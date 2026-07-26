package il.soulSalttrader.shabbattimes.ui

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon

object FabItems {

    val Search = FabItem(
        title = UiText.Resource(R.string.search_new_location),
        selectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
        unselectedIcon = UiIcon.Resource(R.drawable.add_outlined_24),
        action = FabAction.ToggleSearchOverlay,
    )
}
