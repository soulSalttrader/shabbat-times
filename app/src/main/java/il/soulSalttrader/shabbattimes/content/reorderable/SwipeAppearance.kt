package il.soulSalttrader.shabbattimes.content.reorderable

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIcon

sealed interface SwipeAppearance {
    val icon: UiIcon
    val contentDescription: String?
    val settledColor: Color?
    val backgroundColor: @Composable () -> Color
    val contentColor: @Composable () -> Color

    data object Delete : SwipeAppearance {
        override val icon = UiIcon.Resource(R.drawable.delete_forever)
        override val backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.errorContainer }
        override val contentColor: @Composable () -> Color = { MaterialTheme.colorScheme.onErrorContainer }
        override val contentDescription = "Delete"
        override val settledColor: Color? = Color.Transparent
    }

    data object None : SwipeAppearance {
        override val icon: UiIcon = UiIcon.Resource(R.drawable.sentiment_very_dissatisfied_outlined_192)
        override val contentDescription: String? = null
        override val settledColor: Color? = null
        override val backgroundColor: @Composable (() -> Color) = { Color.Transparent }
        override val contentColor: @Composable (() -> Color) = { Color.Transparent }
    }
}
