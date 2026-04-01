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
fun <T> SwipeToDismissContainer(
    item: T,
    modifier: Modifier = Modifier,
    leftSwipe: SwipeConfig<T>,
    rightSwipe: SwipeConfig<T>,
    content: @Composable () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissState()
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(dismissState.currentValue) {
        when (dismissState.currentValue) {
            SwipeToDismissBoxValue.EndToStart -> if (leftSwipe.isEnabled) showDeleteDialog = true
            SwipeToDismissBoxValue.StartToEnd -> if (rightSwipe.isEnabled) rightSwipe.onSwipe(item)
            else                              -> {}
        }
    }

    SwipeToDismissBox(
        state = dismissState,
        modifier = modifier,
        backgroundContent = {
            SwipeBackground(
                dismissState,
                leftAction = leftSwipe.action,
                rightAction = rightSwipe.action,
            )
        },
        enableDismissFromStartToEnd = rightSwipe.isEnabled,
        enableDismissFromEndToStart = leftSwipe.isEnabled,
    ) { content() }

    if (showDeleteDialog) {
        ExplanatoryDialog(
            message = "This action cannot be undone.",
            title = "Delete item?",
            onConfirmText = "Delete",
            onDismissText = "Undo",
            onConfirm = {
                leftSwipe.onSwipe(item)
                showDeleteDialog = false
            },
            onDismiss = {
                showDeleteDialog = false
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
private fun rememberSwipeToDismissState(
    initialValue: SwipeToDismissBoxValue = SwipeToDismissBoxValue.Settled,
    positionalThreshold: (totalDistance: Float) -> Float = SwipeToDismissBoxDefaults.positionalThreshold,
): SwipeToDismissBoxState = remember {
    SwipeToDismissBoxState(
        initialValue = initialValue,
        positionalThreshold = positionalThreshold,
    )
}