package il.soulSalttrader.shabbattimes.ui.effect

import il.soulSalttrader.shabbattimes.ui.UiText

sealed interface UiEffect {
    data class ShowToast(val message: UiText) : UiEffect

    data class ShowSnackBar(
        val message: UiText,
        val actionLabel: UiText? = null,
        val onAction: (() -> Unit)? = null,
    ) : UiEffect

    data object OpenAppSettings : UiEffect
}