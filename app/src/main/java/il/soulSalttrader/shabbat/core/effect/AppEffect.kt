package il.soulSalttrader.retro.core.effect

sealed interface AppEffect {
    sealed interface Breathe : AppEffect {
        data object StartLoop : Breathe
        data object StopLoop : Breathe
    }

    sealed interface Timer : AppEffect {
        data object StartLoop : Timer
        data object StopLoop : Timer
    }
}