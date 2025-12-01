package il.soulSalttrader.retro.shabbatApp.model

sealed interface ShabbatUiState {
    data object Loading : ShabbatUiState
    data class Success(val data: Shabbat) : ShabbatUiState
    data class Error(val message: String, val throwable: Throwable? = null) : ShabbatUiState
}