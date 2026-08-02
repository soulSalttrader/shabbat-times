package il.soulSalttrader.shabbattimes.ui.shabbat

sealed interface CardAction {
    data object OpenGpsSearch : CardAction
    data object ShowDeniedRationale : CardAction
    data object ShowDeniedPermanently : CardAction
    data object None : CardAction
    data object PermissionRequested : CardAction
}
