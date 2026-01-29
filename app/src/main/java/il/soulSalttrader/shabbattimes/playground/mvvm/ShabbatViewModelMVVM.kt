package il.soulSalttrader.shabbattimes.playground.mvvm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.shabbatApp.network.NetworkResult
import il.soulSalttrader.shabbattimes.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.delay
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

    private val debounceJob = SupervisorJob()

    init { refresh() }

    fun retry() {
        debounceJob.cancelChildren()
        viewModelScope.launch(debounceJob) {
            delay(300)
            refresh()
        }
    }

    private fun refresh() {
        if (_isLoading.value) {
            Log.d("ShabbatMVVM", "Refresh already in progress – skipping duplicate call")
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            runCatching {
                repository.getHalachicTimes()
            }.fold(
                onSuccess = { result ->
                    Log.d("HalachicVM", "Load success")
                    _halachicTimes.value = (result as NetworkResult.Success).data
                },
                onFailure = { exception ->
                    val msg = exception.message ?: "Failed to load Halachic times"
                    Log.e("HalachicVM", msg, exception)
                    _errorMessage.value = msg
                }
            )

            _isLoading.value = false
        }
    }
}