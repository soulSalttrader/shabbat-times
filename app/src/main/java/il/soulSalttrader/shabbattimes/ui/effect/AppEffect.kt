package il.soulSalttrader.shabbattimes.ui.effect

sealed interface AppEffect {
    data class ShowToast(val message: String) : AppEffect
    data object OpenAppSettings : AppEffect
}