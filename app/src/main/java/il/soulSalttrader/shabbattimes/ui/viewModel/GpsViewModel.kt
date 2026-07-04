package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.common.userMessage
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.GpsEvent
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.gps.GpsResultState
import il.soulSalttrader.shabbattimes.ui.gps.GpsUiState
import il.soulSalttrader.shabbattimes.useCase.ResolveGpsLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.UpdateCurrentLocationUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet

@HiltViewModel
class GpsViewModel @Inject constructor(
    private val updateCurrentLocationUseCase: UpdateCurrentLocationUseCase,
    resolveGpsLocationUseCase: ResolveGpsLocationUseCase,
    oneTimeMessageTracker: OneTimeMessageTracker,
) : BaseViewModel(oneTimeMessageTracker) {

    private val _state: MutableStateFlow<GpsUiState> = MutableStateFlow(GpsUiState())

    val state: StateFlow<GpsUiState> = resolveGpsLocationUseCase()
        .onEach { resultState ->
            if (resultState is GpsResultState.Resolved) { updateCurrentLocationUseCase(resultState.location) }
        }
        .catch { cause ->
            emitEffect(UiEffect.ShowToast(cause.userMessage()))
            emit(GpsResultState.Failure(cause))
        }
        .map { GpsUiState(gpsResult = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = GpsUiState()
        )

    fun dispatch(event: UiEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is GpsEvent -> event.reducer reduce current
                else        -> current
            }
        }
    }
}