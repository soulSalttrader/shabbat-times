package il.soulSalttrader.shabbattimes.content.reorderable

data class SwipeConfig<T>(
    val toLeft: SwipeState = SwipeState.None,
    val toRight: SwipeState = SwipeState.None,
    val onSwipe: (T) -> Unit = {},
)
