package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIconImage

@Composable
fun SwipeBackground(
    dismissState: SwipeToDismissBoxState,

    leftActionColor: Color = MaterialTheme.colorScheme.errorContainer,
    leftActionIcon: UiIcon = UiIcon.Resource(R.drawable.delete_forever),
    leftIconColor: Color = MaterialTheme.colorScheme.onErrorContainer,
    leftContentDescription: String? = "Delete forever",

    // Right swipe (optional)
    rightActionColor: Color? =  null,
    rightActionIcon: UiIcon? =  null,
    rightIconColor: Color? =  null,
    rightContentDescription: String? = null,

    settledColor: Color = Color.Transparent,
) {
    val direction = dismissState.dismissDirection

    val backgroundColor = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> rightActionColor ?: leftActionColor
        SwipeToDismissBoxValue.EndToStart -> leftActionColor
        SwipeToDismissBoxValue.Settled    -> settledColor
    }

    val iconColor = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> rightIconColor ?: leftIconColor
        SwipeToDismissBoxValue.EndToStart -> leftIconColor
        SwipeToDismissBoxValue.Settled    -> settledColor
    }

    val icon = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> rightActionIcon ?: leftActionIcon
        SwipeToDismissBoxValue.EndToStart -> leftActionIcon
        SwipeToDismissBoxValue.Settled    -> leftActionIcon
    }

    val contentDescription = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> rightContentDescription
        SwipeToDismissBoxValue.EndToStart -> leftContentDescription
        SwipeToDismissBoxValue.Settled    -> null
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(backgroundColor, RoundedCornerShape(16.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = when (direction) {
            SwipeToDismissBoxValue.EndToStart -> Arrangement.End
            SwipeToDismissBoxValue.StartToEnd -> Arrangement.Start
            else                              -> Arrangement.Center
        }
    ) {

        UiIconImage(
            icon = icon,
            contentDescription = contentDescription,
            contentColor = iconColor,
        )
    }
}