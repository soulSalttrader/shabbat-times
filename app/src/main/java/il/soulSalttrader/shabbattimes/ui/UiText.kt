package il.soulSalttrader.shabbattimes.ui

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Resource(@param:StringRes val id: Int) : UiText
    data class Dynamic(val value: String) : UiText

    @Composable
    fun asString() = when (this) {
        is Resource -> stringResource(id)
        is Dynamic  -> value
    }
}