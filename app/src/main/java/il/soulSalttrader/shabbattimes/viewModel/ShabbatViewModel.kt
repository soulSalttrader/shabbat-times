package il.soulSalttrader.shabbattimes.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.LocationEvent
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.model.ShabbatState
import il.soulSalttrader.shabbattimes.model.toLoadedEvent
import il.soulSalttrader.shabbattimes.repository.CityRepository
import il.soulSalttrader.shabbattimes.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val shabbatRepository: ShabbatRepository,
    private val cityRepository: CityRepository,
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
                else -> current
            }
        }

        when (event) {
            is ShabbatDataEvent.Load                -> _effects.tryEmit(AppEffect.Shabbat.LoadData)
            is PermissionEvent.RequestedAppSettings -> _effects.tryEmit(AppEffect.Shabbat.OpenAppSettings)

            else                                    -> Unit
        }
    }

    private fun handleEffects() {
        viewModelScope.launch {
            _effects.collect { effect ->
                when (effect) {
                    is AppEffect.Shabbat.LoadData -> {
                        if (Debug.enabled) Log.d("ShabbatVM", "→ Processing LoadData effect")
                        loadData()
                    }

                    is AppEffect.Shabbat.LoadFailed -> {
                        handleShabbatLoadFailed(effect)
                        if (Debug.enabled) Log.d("ShabbatVM", "→ Processing LoadFailed: ${effect.error.message}")
                    }

                    else -> if (Debug.enabled) Log.w("ShabbatVM", "Unhandled effect: $effect")
                }
            }
        }
    }

    private fun loadData() {
        if (_state.value.data is ShabbatDataState.Loading) {
            if (Debug.enabled) Log.d("ShabbatVM", "Load already in progress – skipping")
            return
        }

        _state.update { current ->
            current.copy(data = ShabbatDataState.Loading)
        }

        viewModelScope.launch {
            shabbatRepository
                .getHalachicTimesForCities(cityRepository.cities)
                .map { it.toLoadedEvent() }
                .catch { exception ->
                    if (Debug.enabled) Log.e("ShabbatVM", "Load failed", exception)

                    dispatch(
                        ShabbatDataEvent.Loaded.Failure(
                            message = exception.message ?: "Unknown error",
                            cause = exception
                        )
                    )
                    _effects.tryEmit(AppEffect.Shabbat.LoadFailed(exception))
                }
                .collectLatest { event ->
                    if (Debug.enabled) Log.d("ShabbatVM", "Load success")
                    dispatch(event)
                }
        }
    }

    private fun handleShabbatLoadFailed(effect: AppEffect.Shabbat.LoadFailed) {
        if (Debug.enabled) Log.d("handleEffects", "${effect.error}")
    }
}