package il.soulSalttrader.shabbattimes.content.reorderable

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

fun <T> LazyListScope.reorderableList(
    state: ReorderableLazyListState,
    items: List<T>,
    header: String,
    keyOf: (T) -> Any,
    swipeConfig: SwipeConfig<T>,
    content: @Composable (T, Modifier) -> Unit,
) {
    item(header) {
        Text(
            text = header,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        )
    }

    items(items, key = { keyOf(it) }) { item ->
        ReorderableItem(
            state = state,
            key = keyOf(item),
        ) {
            SwipeableItem(
                item = item,
                swipeConfig = swipeConfig,
            ) {
                content(item, Modifier.draggableHandle())
            }
        }
    }
}

class ReorderableState<T>(
    items: List<T>,
    val lazyListState: LazyListState,
    val keyOf: (T) -> Any,
) {
    var list by mutableStateOf(items)
    lateinit var reorderableState: ReorderableLazyListState

    fun updateList(updated: List<T>) {
        list = mergeWithCurrentOrder(current = list, updated = updated, keyOf = keyOf)
    }
}

fun <T> mergeWithCurrentOrder(
    current: List<T>,
    updated: List<T>,
    keyOf: (T) -> Any,
): List<T> {
    val updatedKeys = updated.map { keyOf(it) }.toSet()
    return buildList {
        addAll(current.filter { keyOf(it) in updatedKeys })
        addAll(updated.filter { new -> current.none { keyOf(it) == keyOf(new) } })
    }
}

@Composable
fun <T> rememberReorderableState(
    items: List<T>,
    keyOf: (T) -> Any,
): ReorderableState<T> {
    val lazyListState  = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current
    val state          = remember { ReorderableState(items, lazyListState, keyOf) }

    state.reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        state.list = state.list.toMutableList()
            .apply { add(to.index - 1, removeAt(from.index - 1)) }
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    return state
}