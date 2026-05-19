package il.soulSalttrader.shabbattimes.ui.effect

import androidx.annotation.StringRes

sealed interface AppEffect {
    data class ShowToast(val message: String) : AppEffect

    data class ShowSnackBar(
        @param:StringRes val messageRes: Int,
        val messageArgs: List<Any> = emptyList(),
        @param:StringRes val actionLabelRes: Int? = null,
        val onAction: (() -> Unit)? = null,
    ) : AppEffect

    data object OpenAppSettings : AppEffect
}