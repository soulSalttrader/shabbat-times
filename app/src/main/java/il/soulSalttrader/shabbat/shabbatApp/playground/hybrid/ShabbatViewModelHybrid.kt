package il.soulSalttrader.retro.shabbatApp.playground.hybrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModelHybrid @Inject constructor(
    private val repository: ShabbatRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ShabbatUiState> = MutableStateFlow(ShabbatUiState.Loading)
    val state = _state.asStateFlow()

    init { refresh() }

    fun retry() = refresh()

    private fun refresh() {
        viewModelScope.launch {
            _state.value = ShabbatUiState.Loading

            _state.value = when (val result = repository.getHalachicTimes()) {
                is NetworkResult.Success -> {
                    ShabbatUiState.Success(data = result.data)
                }

                is NetworkResult.Failure -> {
                    ShabbatUiState.Failure(message = result.message, cause = result.cause)
                }
            }
        }
    }
}