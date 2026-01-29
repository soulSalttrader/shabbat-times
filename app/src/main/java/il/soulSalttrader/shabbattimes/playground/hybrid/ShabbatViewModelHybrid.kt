package il.soulSalttrader.shabbattimes.playground.hybrid

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.shabbatApp.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.shabbatApp.network.NetworkResult
import il.soulSalttrader.shabbattimes.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModelHybrid @Inject constructor(
    private val repository: ShabbatRepository
) : ViewModel() {
    private val _state: MutableStateFlow<ShabbatDataState> = MutableStateFlow(ShabbatDataState.Loading)
    val state = _state.asStateFlow()

    init { refresh() }

    fun retry() = refresh()

    private fun refresh() {
        viewModelScope.launch {
            _state.value = ShabbatDataState.Loading

            _state.value = when (val result = repository.getHalachicTimes()) {
                is NetworkResult.Success -> {
                    ShabbatDataState.Success(data = result.data)
                }

                is NetworkResult.Failure -> {
                    ShabbatDataState.Failure(message = result.message, cause = result.cause)
                }
            }
        }
    }
}