package il.soulSalttrader.shabbattimes.content.reorderable

sealed interface SwipeState {
    val isEnabled: Boolean
    val appearance: SwipeAppearance
    val requiresConfirmation: Boolean

    data object Delete : SwipeState {
        override val isEnabled: Boolean = true
        override val appearance: SwipeAppearance = SwipeAppearance.Delete
        override val requiresConfirmation: Boolean = true
    }

    data object None : SwipeState {
        override val isEnabled: Boolean = false
        override val appearance: SwipeAppearance = SwipeAppearance.None
        override val requiresConfirmation: Boolean = false
    }
}