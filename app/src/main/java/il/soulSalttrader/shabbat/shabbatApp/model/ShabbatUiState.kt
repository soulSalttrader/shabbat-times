package il.soulSalttrader.retro.shabbatApp.model

sealed interface ShabbatUiState {
    data object Loading : ShabbatUiState
    data class Success(val data: Shabbat) : ShabbatUiState
    data class Failure(val message: String, val cause: Throwable? = null) : ShabbatUiState
}