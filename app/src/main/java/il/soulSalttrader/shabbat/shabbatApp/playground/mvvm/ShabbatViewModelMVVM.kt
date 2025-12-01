package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModelMVVM @Inject constructor(
    private val repository: ShabbatRepository
) : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatUiStateMVVM> = MutableStateFlow(ShabbatUiStateMVVM())
    val uiState: StateFlow<ShabbatUiStateMVVM> = _uiState.asStateFlow()

    init { refresh() }

    fun retry() = refresh()

    private fun refresh() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            _uiState.value = when (val result = repository.getShabbatTimes()) {
                is NetworkResult.Success -> {
                    if (Debug.enabled) Log.d("ShabbatViewModelMVVM.refresh", "${result.data}")

                    ShabbatUiStateMVVM(results = result.data, isLoading = false)
                }

                is NetworkResult.Failure -> {
                    if (Debug.enabled) Log.d("ShabbatViewModelMVVM.refresh", "message: ${result.message}, cause: ${result.cause}")

                    ShabbatUiStateMVVM(isLoading = false, errorMessage = result.message)
                }
            }
        }
    }
}