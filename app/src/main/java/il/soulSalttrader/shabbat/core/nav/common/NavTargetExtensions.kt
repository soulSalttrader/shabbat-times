package il.soulSalttrader.retro.core.nav.common

import il.soulSalttrader.retro.core.nav.NavTarget

fun NavTarget.simpleName(): String =
    requireNotNull(this::class.simpleName) {
        "NavTarget object has no simpleName: $this. Must be top-level 'object'."
    }