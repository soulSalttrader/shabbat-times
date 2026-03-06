package il.soulSalttrader.shabbattimes.effect

sealed interface AppEffect {
    data class ShowToast(val message: String) : AppEffect
    data object OpenAppSettings : AppEffect
}