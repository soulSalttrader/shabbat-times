package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun <T> SwipeToDismissContainer(
    item: T,
    modifier: Modifier = Modifier,

    onSwipeLeft: (T) -> Unit,
    leftSwipeEnabled: Boolean = true,
    leftAction: SwipeAction = SwipeAction.Delete,

    onSwipeRight: (T) -> Unit = {},
    rightSwipeEnabled: Boolean = false,
    rightAction: SwipeAction? = null,

    content: @Composable () -> Unit,
) {
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )

    val dismissValue: (SwipeToDismissBoxValue) -> Unit = { dismissedValue ->
        when (dismissedValue) {
            SwipeToDismissBoxValue.EndToStart -> { onSwipeLeft(item) }
            SwipeToDismissBoxValue.StartToEnd -> { onSwipeRight(item) }
            else                              -> {}
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        onDismiss = dismissValue,
        backgroundContent = {
            SwipeBackground(
                dismissState,
                leftAction = leftAction,
                rightAction = rightAction,
            )
        },
        enableDismissFromStartToEnd = rightSwipeEnabled,
        enableDismissFromEndToStart = leftSwipeEnabled,
    ) { content() }
}
