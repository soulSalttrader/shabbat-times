# Reorderable Cards

Cards support drag-to-reorder and swipe-to-delete, implemented as a generic, self-contained container wrapping any card.

## Drag & Drop

Long-press the drag handle icon to enter drag mode and reorder cards freely.

## Swipe to Delete

Swipe a card left to trigger deletion via `LocationDeleted` event.

## Implementation Notes

- `ReorderableState<T>` — generic state holder for drag + list management
- `rememberReorderableState()` — composable that wires `ReorderableLazyListState` with haptic feedback
- `ItemContent<T>` — `@Stable` functional interface for item content, prevents unnecessary recomposition
- `ImmutableList<T>` — required for Compose stability, ensures `LazyColumn` items recompose correctly
- `mergeWithCurrentOrder()` — merges ViewModel-emitted list with current drag order, preserving position but always using latest item data

## Usage
The reorderable container is generic and composable — it works with any item type and plugs into an existing LazyColumn:

```kotlin
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
```

then

```kotlin
@Composable
fun ShabbatContent(
    items: ImmutableList<ShabbatEntry>,
    swipeConfig: SwipeConfig<ShabbatEntry> = SwipeConfig(),
    searchConfig: SearchConfig,
    isDraggable: Boolean = true,

    onClick: () -> Unit = {},
    onReorder: (from: Int, to: Int) -> Unit = {_, _ ->},
) {
    val state = rememberReorderableState(items = items, onReorder = onReorder)
    val header = stringResource(R.string.shabbat_my_locations)

    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = state.lazyListState,
            contentPadding = PaddingValues(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            reorderableList(
                state = state,
                header = header,
                items = state.list,
                keyOf = { it.location.id },
                swipeConfig = swipeConfig,
            ) { item, modifier ->
                ShabbatCard(
                    modifier = modifier, // drag modifier
                    testTag = when (item.location.id) {
                        SavedLocation.GPS_ID   -> TestTags.GPS_CARD
                        SavedLocation.EMPTY_ID -> TestTags.EMPTY_CARD
                        else                   -> TestTags.LOCATION_CARD
                    },
                    item = item,
                    isDraggable = isDraggable,
                    onClick = { onClick() }
                )
            }
        }

        AnimatedSearchScrim(searchConfig = searchConfig)
        AnimatedSearchOverlay(searchConfig = searchConfig)
        AnimatedSearchFab(searchConfig = searchConfig)
    }
}
```

Both draggable and swipeable can be toggled independently via parameters, making the container reusable across different screens with different interaction needs.
