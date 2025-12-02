package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import il.soulSalttrader.retro.shabbatApp.model.SolarTimes
import kotlinx.serialization.Serializable

@Serializable
data class ShabbatUiStateMVVM(
    val results: SolarTimes = SolarTimes(),
    val status: String = "",
    val errorMessage: String? = null,
    val isLoading: Boolean = false,
)