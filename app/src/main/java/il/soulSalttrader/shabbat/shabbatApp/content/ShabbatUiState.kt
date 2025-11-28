package il.soulSalttrader.retro.shabbatApp.content

import il.soulSalttrader.retro.shabbatApp.model.ShabbatResult

interface ShabbatUiState {
    data object Loading : ShabbatUiState
    data class Success(val result: ShabbatResult) : ShabbatUiState
    data class Error(val message: String) : ShabbatUiState
}