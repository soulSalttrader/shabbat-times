package il.soulSalttrader.shabbattimes.content.reorderable

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import il.soulSalttrader.shabbattimes.content.ExplanatoryDialog
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeableItem(
    item: T,
    modifier: Modifier = Modifier,
    swipeConfig: SwipeConfig<T>,
    message: String = "This action cannot be undone.",
    title: String = "Delete item?",
    onConfirmText: String = "Delete",
    onDismissText: String = "Undo",
    content: @Composable () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeItemState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> if (swipeConfig.toLeft.isEnabled) showDialog = true
            SwipeToDismissBoxValue.StartToEnd -> if (swipeConfig.toRight.isEnabled) swipeConfig.onSwipe(item)
            else                              -> {}
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            SwipeBackground(
                state = dismissState,
                swipeConfig = swipeConfig,
            )
        },
        enableDismissFromStartToEnd = swipeConfig.toRight.isEnabled,
        enableDismissFromEndToStart = swipeConfig.toLeft.isEnabled,
    ) { content() }

    if (showDialog) {
        ExplanatoryDialog(
            message = message,
            title = title,
            onConfirmText = onConfirmText,
            onDismissText = onDismissText,
            onConfirm = {
                swipeConfig.onSwipe(item)
                showDialog = false
            },
            onDismiss = {
                showDialog = false
                coroutineScope.launch { dismissState.reset() }
            },
            onConfirmColor = { MaterialTheme.colorScheme.error },
            onDismissColor = { MaterialTheme.colorScheme.primary },
        )
    }
}


/**
 * Remembers a [SwipeToDismissBoxState] **without** using [androidx.compose.runtime.saveable.rememberSaveable].
 *
 * Use this instead of [androidx.compose.material3.rememberSwipeToDismissBoxState] when items can be dynamically
 * removed and re-added to the list, to prevent stale swipe state from being restored.
 */
@Composable
private fun rememberSwipeItemState(
    initialValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.Settled,
    positionalThreshold: (totalDistance: Float) -> Float = SwipeToDismissBoxDefaults.positionalThreshold,
): SwipeToDismissBoxState = remember {
    SwipeToDismissBoxState(
        initialValue = initialValue,
        positionalThreshold = positionalThreshold,
    )
}