package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIcon

sealed interface SwipeAction {
    val icon: UiIcon
    val contentDescription: String?
    val settledColor: Color?
    val backgroundColor: @Composable () -> Color
    val contentColor: @Composable () -> Color

    data class Custom(
        override val icon: UiIcon,
        override val backgroundColor: @Composable () -> Color,
        override val contentColor: @Composable () -> Color,
        override val contentDescription: String? = null,
        override val settledColor: Color?
    ) : SwipeAction

    object Delete : SwipeAction {
        override val icon = UiIcon.Resource(R.drawable.delete_forever)
        override val backgroundColor: @Composable () -> Color = { MaterialTheme.colorScheme.errorContainer }
        override val contentColor: @Composable () -> Color = { MaterialTheme.colorScheme.onErrorContainer }
        override val contentDescription = "Delete"
        override val settledColor: Color? = Color.Transparent
    }
}
