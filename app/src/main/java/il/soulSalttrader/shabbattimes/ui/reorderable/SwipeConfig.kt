package il.soulSalttrader.shabbattimes.ui.reorderable

data class SwipeConfig<T>(
    val toLeft: SwipeState = SwipeState.None,
    val toRight: SwipeState = SwipeState.None,
    val onSwipe: (T) -> Unit = {},
)
