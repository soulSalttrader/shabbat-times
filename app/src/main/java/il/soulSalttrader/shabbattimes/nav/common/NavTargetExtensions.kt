package il.soulSalttrader.shabbattimes.nav.common

import il.soulSalttrader.shabbattimes.nav.NavItems.navItemsByTarget
import il.soulSalttrader.shabbattimes.nav.NavTarget

fun NavTarget.route(): String =
    requireNotNull(this::class.qualifiedName) {
        "NavTarget object has no route: $this. Must be top-level 'object'."
    }

fun NavTarget.titleOr(default: String = "No title"): String =
    navItemsByTarget[this]?.title ?: default