package il.soulSalttrader.retro.shabbatApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val repository: ShabbatRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatUiState> = MutableStateFlow(ShabbatUiState.Loading)
    val uiState: StateFlow<ShabbatUiState> = _uiState.asStateFlow()

    init { refresh() }

    fun retry() = refresh()

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = ShabbatUiState.Loading

            _uiState.value = when (val result = repository.getShabbatTimes()) {
                is NetworkResult.Success -> {
                    if (Debug.enabled) Log.d("ShabbatViewModel.refresh", "${result.data}")

                    ShabbatUiState.Success(data = result.data)
                }

                is NetworkResult.Failure -> {
                    if (Debug.enabled) Log.d("ShabbatViewModel.refresh", "message: ${result.message}, cause: ${result.cause}")

                    ShabbatUiState.Failure(message = result.message, cause = result.cause)
                }
            }
        }
    }
}