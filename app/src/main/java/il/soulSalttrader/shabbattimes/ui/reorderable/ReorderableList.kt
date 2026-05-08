package il.soulSalttrader.shabbattimes.ui.reorderable

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
import il.soulSalttrader.shabbattimes.ui.ItemContent
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState
import sh.calvin.reorderable.rememberReorderableLazyListState

fun <T> LazyListScope.reorderableList(
    state: ReorderableLazyListState,
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
            state = state,
            key = keyOf(item),
        ) {
            SwipeableItem(
                item = item,
                swipeConfig = swipeConfig,
            ) {
                content.Content(item, Modifier.draggableHandle())
            }
        }
    }
}

class ReorderableState<T>(
    items: ImmutableList<T>,
    val lazyListState: LazyListState,
    val keyOf: (T) -> Any,
) {
    var list by mutableStateOf(items)
    lateinit var reorderableState: ReorderableLazyListState

    fun updateList(updated: ImmutableList<T>) {
        list = mergeWithCurrentOrder(current = list, updated = updated, keyOf = keyOf)
    }
}

// TODO: remove when Room is implemented — order will be persisted in DB
// temporarily preserves drag order across ViewModel emissions
fun <T> mergeWithCurrentOrder(
    current: ImmutableList<T>,
    updated: ImmutableList<T>,
    keyOf: (T) -> Any,
): ImmutableList<T> {
    val updatedMap = updated.associateBy { keyOf(it) }
    return buildList {
        // new items not in current — add at top
        addAll(updated.filter { new -> current.none { keyOf(it) == keyOf(new) } })
        // existing items — preserve ORDER from current but DATA from updated
        addAll(current.mapNotNull { updatedMap[keyOf(it)] })
    }.toImmutableList()
}

@Composable
fun <T> rememberReorderableState(
    items: ImmutableList<T>,
    keyOf: (T) -> Any,
): ReorderableState<T> {
    val lazyListState = rememberLazyListState()
    val hapticFeedback = LocalHapticFeedback.current
    val state = remember { ReorderableState(items, lazyListState, keyOf) }

    state.reorderableState = rememberReorderableLazyListState(lazyListState) { from, to ->
        state.list = state.list.toMutableList()
            .apply { add(to.index - 1, removeAt(from.index - 1)) }.toImmutableList()
        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
    }

    return state
}