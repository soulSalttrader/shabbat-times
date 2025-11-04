package il.soulSalttrader.retro.core

import il.soulSalttrader.retro.BuildConfig

object Debug {
    val enabled: Boolean = runCatching {
        Class.forName("il.soulSalttrader.retro.BuildConfig")
        BuildConfig.DEBUG
    }.getOrDefault(true)
}