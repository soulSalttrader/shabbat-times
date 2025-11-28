package il.soulSalttrader.retro.shabbatApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.shabbatApp.api.RetrofitInstance
import il.soulSalttrader.retro.shabbatApp.model.ShabbatResponse
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor() : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatResponse> = MutableStateFlow(ShabbatResponse())
    val uiState: StateFlow<ShabbatResponse> = _uiState.asStateFlow()

    init { loadShabbatTimes() }

    private fun loadShabbatTimes() {
        viewModelScope.launch {
            runCatching {
                RetrofitInstance.api.getShabbatTimes()
            }.onSuccess { response ->
                _uiState.value = response
                if (Debug.enabled) Log.d("ShabbatViewModel.getShabbatTimes", response.status)
            }.onFailure { e ->
                Log.d("ShabbatViewModel.getShabbatTimes", "${e.message}")
            }
        }
    }
}