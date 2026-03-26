package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxDefaults
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> SwipeToDismissContainer(
    item: T,
    modifier: Modifier = Modifier,

    onSwipeLeft: (T) -> Unit,
    leftSwipeEnabled: Boolean = true,
    leftAction: SwipeAction = SwipeAction.Delete,
    onDeleteConfirmed: (T) -> Unit,

    onSwipeRight: (T) -> Unit = {},
    rightSwipeEnabled: Boolean = false,
    rightAction: SwipeAction? = null,

    content: @Composable () -> Unit,
) {
    var showDeleteDialog by remember { mutableStateOf(false) }
    val dismissState = rememberSwipeToDismissBoxState(
        initialValue = SwipeToDismissBoxValue.Settled,
        positionalThreshold = SwipeToDismissBoxDefaults.positionalThreshold
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(dismissState.currentValue) {
        if (dismissState.currentValue == SwipeToDismissBoxValue.EndToStart) {
            showDeleteDialog = true
        }
    }

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

    if (showDeleteDialog) {
        ExplanatoryDialog(
            message = "This action cannot be undone.",
            title = "Delete item?",
            onConfirmText = "Delete",
            onDismissText = "Undo",
            onConfirm = {
                onDeleteConfirmed(item)
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