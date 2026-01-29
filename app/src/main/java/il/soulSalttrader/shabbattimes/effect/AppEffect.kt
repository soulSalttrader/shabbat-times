package il.soulSalttrader.shabbattimes.effect

sealed interface AppEffect {
    sealed interface Shabbat : AppEffect {
        data object LoadData : Shabbat
        data class LoadFailed(val error: Throwable) : Shabbat
        data object OpenAppSettings : AppEffect
    }
}