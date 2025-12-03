package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import kotlinx.serialization.Serializable

@Serializable
data class ShabbatUiStateMVVM(
    val results: HalachicTimesDisplay = HalachicTimesDisplay(),
    val status: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)