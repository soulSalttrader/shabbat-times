package il.soulSalttrader.shabbattimes.ui.effect

import il.soulSalttrader.shabbattimes.ui.UiText

sealed interface AppEffect {
    data class ShowToast(val message: UiText) : AppEffect

    data class ShowSnackBar(
        val message: UiText,
        val actionLabel: UiText? = null,
        val onAction: (() -> Unit)? = null,
    ) : AppEffect

    data object OpenAppSettings : AppEffect
}