package il.soulSalttrader.shabbattimes.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

data class DialogButtonAction(
    val text: String,
    val onClick: () -> Unit,
    val color: (@Composable () -> Color)? = null,
)
