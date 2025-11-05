package il.soulSalttrader.retro.core

import il.soulSalttrader.retro.BuildConfig

object Debug {
    val enabled: Boolean = runCatching {
        Class.forName("il.soulSalttrader.retro.BuildConfig")
        BuildConfig.DEBUG
    }.getOrDefault(true)

    fun <T : State> T.formatStateTransition(
        eventName: String,
        next: T,
    ): String = "${logTag()}(event=$eventName) ➜ $next"

    fun <T : State> Reducer<T>.formatEventName(): String =
        javaClass.enclosingClass.simpleName
}