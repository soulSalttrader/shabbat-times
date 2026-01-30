package il.soulSalttrader.shabbattimes.nav.common

import il.soulSalttrader.shabbattimes.nav.NavTarget

fun NavTarget.simpleName(): String =
    requireNotNull(this::class.simpleName) {
        "NavTarget object has no simpleName: $this. Must be top-level 'object'."
    }

fun NavTarget.route(): String =
    requireNotNull(this::class.qualifiedName) {
        "NavTarget object has no route: $this. Must be top-level 'object'."
    }