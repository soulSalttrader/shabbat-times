package il.soulSalttrader.retro.shabbatApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.effect.AppEffect
import il.soulSalttrader.retro.core.event.ShabbatEvent
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.model.toLoadedEvent
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val repository: ShabbatRepository,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatUiState> = MutableStateFlow(value = ShabbatUiState.Loading)
    val uiState: StateFlow<ShabbatUiState> = _uiState.asStateFlow()

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    init {
        handleEffects()
        _effects.tryEmit(AppEffect.Shabbat.LoadData)
    }

    fun dispatch(event: ShabbatEvent) {
        when (event) {
            is ShabbatEvent.Load -> {
                _uiState.update { current -> event.reducer reduce current }
                _effects.tryEmit(AppEffect.Shabbat.LoadData)
            }
            is ShabbatEvent.Loaded -> {
                _uiState.update { current -> event.reducer reduce current }
            }
        }
    }

    private fun handleEffects() {
        viewModelScope.launch {
            _effects.collect { effect ->
                when (effect) {
                    is AppEffect.Shabbat.LoadData   -> loadData()
                    is AppEffect.Shabbat.LoadFailed -> handleShabbatLoadFailed(effect)
                    else                            -> throw IllegalArgumentException("Unknown effect: $effect.")
                }
            }
        }
    }

    private suspend fun loadData() {
        runCatching {
            repository.getHalachicTimes()
        }.onSuccess { result ->
            dispatch(event = result.toLoadedEvent())
        }.onFailure { exception ->
            _effects.tryEmit(AppEffect.Shabbat.LoadFailed(exception))
        }
    }

    private fun handleShabbatLoadFailed(effect: AppEffect.Shabbat.LoadFailed) {
        Log.d("handleEffects", "${effect.error}")
    }
}