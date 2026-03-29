package il.soulSalttrader.shabbattimes.content

data class SwipeConfig<T>(
    val isEnabled: Boolean,
    val action: SwipeAction,
    val requiresConfirmation: Boolean,
    val onSwipe: (T) -> Unit,
)