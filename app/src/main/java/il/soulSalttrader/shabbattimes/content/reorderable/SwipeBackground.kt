package il.soulSalttrader.shabbattimes.content.reorderable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.content.uiIcon.UiIconImage

@Composable
fun SwipeBackground(
    dismissState: SwipeToDismissBoxState,
    leftAction: SwipeAction,
    rightAction: SwipeAction?,
) {
    val direction = dismissState.dismissDirection
    val action = when (direction) {
        SwipeToDismissBoxValue.StartToEnd -> rightAction ?: leftAction
        else -> leftAction
    }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
            .background(action.backgroundColor(), RoundedCornerShape(16.dp))
            .padding(24.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = when (direction) {
            SwipeToDismissBoxValue.EndToStart -> Arrangement.End
            SwipeToDismissBoxValue.StartToEnd -> Arrangement.Start
            else                              -> Arrangement.Center
        }
    ) {

        UiIconImage(
            icon = action.icon,
            contentDescription = action.contentDescription,
            contentColor = action.contentColor(),
        )
    }
}