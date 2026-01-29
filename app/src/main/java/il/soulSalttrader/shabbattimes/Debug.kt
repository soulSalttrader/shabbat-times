package il.soulSalttrader.shabbattimes

import il.soulSalttrader.shabbattimes.BuildConfig
import il.soulSalttrader.shabbattimes.model.State
import il.soulSalttrader.shabbattimes.reducer.Reducer

object Debug {
    val enabled: Boolean = runCatching {
        Class.forName("il.soulSalttrader.shabbat.BuildConfig")
        BuildConfig.DEBUG
    }.getOrDefault(true)

    fun <S : State> S.formatStateTransition(
        eventName: String,
        next: S,
    ): String = "${logTag()}(event=$eventName) ➜ $next"

    fun <S : State> Reducer<S>.formatEventName(): String =
        javaClass.enclosingClass.simpleName
}