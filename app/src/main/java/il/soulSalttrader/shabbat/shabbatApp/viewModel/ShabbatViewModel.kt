package il.soulSalttrader.retro.shabbatApp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.retro.core.effect.AppEffect
import il.soulSalttrader.retro.core.event.AppEvent
import il.soulSalttrader.retro.core.event.LocationEvent
import il.soulSalttrader.retro.core.event.PermissionEvent
import il.soulSalttrader.retro.core.event.ShabbatDataEvent
import il.soulSalttrader.retro.shabbatApp.model.ShabbatState
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
    private val _state: MutableStateFlow<ShabbatState> = MutableStateFlow(value = ShabbatState())
    val state: StateFlow<ShabbatState> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    init {
        handleEffects()
        _effects.tryEmit(AppEffect.Shabbat.LoadData)
    }

    fun dispatch(event: AppEvent) {
        _state.update { current ->
            when (event) {
                is ShabbatDataEvent -> event.reducer reduce current
                is PermissionEvent  -> event.reducer reduce current
                is LocationEvent    -> event.reducer reduce current

                else                -> current
            }
        }

        when (event) {
            is ShabbatDataEvent.Load -> _effects.tryEmit(AppEffect.Shabbat.LoadData)
            else -> Unit
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