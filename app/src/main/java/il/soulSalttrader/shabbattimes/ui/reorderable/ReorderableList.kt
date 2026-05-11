package il.soulSalttrader.shabbattimes.ui.reorderable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.ui.ItemContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

fun <T> LazyListScope.reorderableList(
    state: ReorderableState<T>,
    items: ImmutableList<T>,
    header: String,
    keyOf: (T) -> Any,
    swipeConfig: SwipeConfig<T>,
    content: ItemContent<T>,
) {
    item(header) {
        Text(
            text = header,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }

    items(items, key = { keyOf(it) }) { item ->
        ReorderableItem(
            state = state.reorderableState,
            key = keyOf(item),
        ) {
            SwipeableItem(
                item = item,
                swipeConfig = swipeConfig,
            ) {
                content.Content(item, Modifier.draggableHandle(
                    onDragStopped = {
                        val from = state.pendingFrom
                        val to = state.pendingTo
                        if (from != -1 && to != -1 && from != to) {
                            state.onReorder(from, to)
                        }
                        state.pendingFrom = -1
                        state.pendingTo = -1
                    }
                ))
            }
        }
    }
}

class ReorderableState<T>(
    items: ImmutableList<T>,
    val lazyListState: LazyListState,
) {
    var list by mutableStateOf(items)
    lateinit var reorderableState: ReorderableLazyListState
    var onReorder: (from: Int, to: Int) -> Unit = { _, _ -> }
    var pendingFrom: Int = -1
    var pendingTo: Int = -1
}

@Composable
fun <T> rememberReorderableState(
    items: ImmutableList<T>,
    onReorder: (from: Int, to: Int) -> Unit,
): ReorderableState<T> {
    val lazyListState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current
    val state = remember { ReorderableState(items, lazyListState) }

    LaunchedEffect(items) {
        state.list = items
    }

    state.reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        state.list = state.list.toMutableList()
            .apply { add(to.index - 1, removeAt(from.index - 1)) }
            .toImmutableList()

        if (state.pendingFrom == -1) state.pendingFrom = from.index - 1
        state.pendingTo = to.index - 1
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    state.onReorder = onReorder

    return state
}