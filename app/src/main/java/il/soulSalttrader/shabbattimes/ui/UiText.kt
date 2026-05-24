package il.soulSalttrader.shabbattimes.ui

import android.content.Context
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource

sealed interface UiText {
    data class Resource(
        @param:StringRes val id: Int,
        val args: List<Any> = emptyList(),
    ) : UiText
    data class Dynamic(val value: String) : UiText

    @Composable
    fun asString(): String = when (this) {
        is Resource -> stringResource(id, *args.toTypedArray())
        is Dynamic -> value
    }

    fun resolve(context: Context): String = when (this) {
        is Resource -> context.getString(id, *args.toTypedArray())
        is Dynamic -> value
    }
}