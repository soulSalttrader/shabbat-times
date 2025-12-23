package il.soulSalttrader.retro.core

import il.soulSalttrader.retro.BuildConfig
import il.soulSalttrader.retro.core.model.State
import il.soulSalttrader.retro.core.reducer.Reducer

object Debug {
    val enabled: Boolean = runCatching {
        Class.forName("il.soulSalttrader.retro.BuildConfig")
        BuildConfig.DEBUG
    }.getOrDefault(true)

    fun <S : State> S.formatStateTransition(
        eventName: String,
        next: S,
    ): String = "${logTag()}(event=$eventName) ➜ $next"

    fun <S : State> Reducer<S>.formatEventName(): String =
        javaClass.enclosingClass.simpleName
}