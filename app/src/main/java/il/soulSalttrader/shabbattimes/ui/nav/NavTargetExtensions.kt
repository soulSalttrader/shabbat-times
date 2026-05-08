package il.soulSalttrader.shabbattimes.ui.nav

import il.soulSalttrader.shabbattimes.ui.nav.NavItems.navItemsByTarget

fun NavTarget.route(): String =
    requireNotNull(this::class.qualifiedName) {
        "NavTarget object has no route: $this. Must be top-level 'object'."
    }

fun NavTarget.titleOr(default: String = "No title"): String =
    navItemsByTarget[this]?.title ?: default