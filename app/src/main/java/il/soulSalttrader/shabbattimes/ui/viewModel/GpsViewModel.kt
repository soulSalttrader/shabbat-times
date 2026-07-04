package il.soulSalttrader.shabbattimes.ui.viewModel

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.common.userMessage
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.GpsEvent
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.gps.GpsResultState
import il.soulSalttrader.shabbattimes.ui.gps.GpsUiState
import il.soulSalttrader.shabbattimes.useCase.ResolveGpsLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.UpdateCurrentLocationUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class GpsViewModel @Inject constructor(
    private val updateCurrentLocationUseCase: UpdateCurrentLocationUseCase,
    private val resolveGpsLocationUseCase: ResolveGpsLocationUseCase,
    oneTimeMessageTracker: OneTimeMessageTracker,
) : BaseViewModel(oneTimeMessageTracker) {

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gpsLocationFlow: StateFlow<ResolvedLocation?> = resolveGpsLocationUseCase()
        .onEach { resolved -> updateCurrentLocationUseCase(resolved) }
        .catch { cause ->
            dispatch(GpsEvent.GpsLocationError(cause))
            emitEffect(UiEffect.ShowToast(cause.userMessage()))
            emit(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )


    private val _state: MutableStateFlow<GpsUiState> = MutableStateFlow(GpsUiState())

    val state: StateFlow<GpsUiState> = combine(
        _state,
        gpsLocationFlow
    )  { state, gsp ->
        gsp?.let { GpsEvent.GpsLocationLoaded(it).reducer reduce state } ?: state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = GpsUiState(),
    )

    init {
        viewModelScope.launch {
            resolveGpsLocationUseCase()
                .collect { resolved ->
                    _state.update { it.copy(gpsResult = when (resolved) {
                        null -> GpsResultState.Idle
                        else -> GpsResultState.Resolved(resolved)
                    })}
                    updateCurrentLocationUseCase(resolved)
                }
        }
    }

    fun dispatch(event: UiEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is GpsEvent -> event.reducer reduce current
                else        -> current
            }
        }
    }
}