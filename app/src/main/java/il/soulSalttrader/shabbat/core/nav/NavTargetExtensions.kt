package il.soulSalttrader.retro.core.nav

fun NavTarget.simpleName(): String =
    requireNotNull(this::class.simpleName) {
        "NavTarget object has no simpleName: $this. Must be top-level 'object'."
    }