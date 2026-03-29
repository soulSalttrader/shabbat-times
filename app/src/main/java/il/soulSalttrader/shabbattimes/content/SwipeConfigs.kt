package il.soulSalttrader.shabbattimes.content

object SwipeConfigs {
    fun <T> swipeToDelete(onSwipe: (T) -> Unit) = SwipeConfig(
        isEnabled = true,
        action = SwipeAction.Delete,
        requiresConfirmation = true,
        onSwipe = onSwipe,
    )

    fun <T> none(): SwipeConfig<T> = SwipeConfig(
        isEnabled = false,
        action = SwipeAction.Delete,
        requiresConfirmation = false,
        onSwipe = {},
    )
}