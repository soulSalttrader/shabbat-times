package il.soulSalttrader.retro.core.model

interface State {
    fun logTag(suffix: String = "State"): String =
        this::class.simpleName?.removeSuffix(suffix = suffix) ?: "UnknownState"
}