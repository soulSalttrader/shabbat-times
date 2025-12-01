package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import il.soulSalttrader.retro.shabbatApp.model.Shabbat
import kotlinx.serialization.Serializable

@Serializable
data class ShabbatUiStateMVVM(
    val results: Shabbat = Shabbat(),
    val status: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)