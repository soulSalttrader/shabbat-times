package il.soulSalttrader.shabbattimes.ui.nav

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.nav.NavItems.navItemsByTarget

fun NavTarget.route(): String =
    requireNotNull(this::class.qualifiedName) {
        "NavTarget object has no route: $this. Must be top-level 'object'."
    }

fun NavTarget.titleOr(default: UiText = UiText.Resource(R.string.nav_no_title)): UiText =
    navItemsByTarget[this]?.title ?: default