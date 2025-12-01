package il.soulSalttrader.retro.shabbatApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.network.RetrofitClient
import il.soulSalttrader.retro.shabbatApp.content.ShabbatUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatUiState> = MutableStateFlow(ShabbatUiState.Loading)
    val uiState: StateFlow<ShabbatUiState> = _uiState.asStateFlow()

    init { loadShabbatTimes() }

    fun retry() = loadShabbatTimes()

    private fun loadShabbatTimes() {
        viewModelScope.launch {
            _uiState.value = ShabbatUiState.Loading

            runCatching {
                RetrofitClient.shabbatApi.getShabbatTimes()
            }.onSuccess { response ->
                if (Debug.enabled) Log.d("ShabbatViewModel.getShabbatTimes", response.status)

                when {
                    response.status.equals("OK", ignoreCase = true) -> {
                        _uiState.value = ShabbatUiState.Success(response.results)
                    }
                    else -> _uiState.value = ShabbatUiState.Error("API returned ${response.status}")
                }

            }.onFailure { exception ->
                _uiState.value = ShabbatUiState.Error(exception.message ?: "Unknown error")
                Log.d("ShabbatViewModel.getShabbatTimes", "${exception.message}")
            }
        }
    }
}