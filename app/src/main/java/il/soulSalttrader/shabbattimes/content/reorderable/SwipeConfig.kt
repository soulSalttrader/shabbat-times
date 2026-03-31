package il.soulSalttrader.shabbattimes.content.reorderable

data class SwipeConfig<T>(
    val isEnabled: Boolean,
    val action: SwipeAction,
    val requiresConfirmation: Boolean,
    val onSwipe: (T) -> Unit,
)