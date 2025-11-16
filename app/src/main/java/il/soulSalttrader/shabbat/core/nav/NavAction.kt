package il.soulSalttrader.retro.core.nav

import kotlinx.serialization.Serializable

sealed interface NavAction {
    @Serializable
    data class To(val target: NavTarget) : NavAction
    @Serializable
    data class ResetTo(val target: NavTarget) : NavAction
    @Serializable
    data class PopTo(val target: NavTarget) : NavAction

    data object Up : NavAction
    data object PopToRoot : NavAction
}