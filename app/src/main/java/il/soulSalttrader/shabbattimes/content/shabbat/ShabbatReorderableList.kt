package il.soulSalttrader.shabbattimes.content.shabbat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.content.SwipeConfigs
import il.soulSalttrader.shabbattimes.content.SwipeToDismissContainer
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.ReorderableLazyListState

fun <T> LazyListScope.section(
    state: ReorderableLazyListState,
    items: List<T>,
    header: String,
    keyOf: (T) -> Any,
    onLeftSwipe: (T) -> Unit,
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
            SwipeToDismissContainer(
                item = item,
                leftSwipe = SwipeConfigs.swipeToDelete(onSwipe = { onLeftSwipe(item) }),
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