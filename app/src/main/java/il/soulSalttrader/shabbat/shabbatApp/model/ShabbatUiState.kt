package il.soulSalttrader.retro.shabbatApp.model

import il.soulSalttrader.retro.core.State

sealed interface ShabbatUiState : State {
    data object Loading : ShabbatUiState
    data class Success(val data: HalachicTimesDisplay) : ShabbatUiState
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatUiState
}