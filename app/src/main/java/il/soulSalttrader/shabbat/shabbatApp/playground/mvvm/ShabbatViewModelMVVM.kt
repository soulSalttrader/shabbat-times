package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
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
    private val _halachicTimes = MutableStateFlow(HalachicTimesDisplay())
    val halachicTimes: StateFlow<HalachicTimesDisplay> = _halachicTimes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init { refresh() }

    fun retry() = refresh()

    private fun refresh() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            when (val result = repository.getHalachicTimes()) {
                is NetworkResult.Success -> {
                    _halachicTimes.value = result.data
                    _isLoading.value = false
                }

                is NetworkResult.Failure -> {
                    _errorMessage.value = result.message
                    _isLoading.value = false
                }
            }
        }
    }
}