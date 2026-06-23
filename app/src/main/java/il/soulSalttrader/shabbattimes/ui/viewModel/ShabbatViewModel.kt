package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.common.userMessage
import il.soulSalttrader.shabbattimes.di.InMemory
import il.soulSalttrader.shabbattimes.di.Persisted
import il.soulSalttrader.shabbattimes.model.HalachicTimes
import il.soulSalttrader.shabbattimes.model.ShabbatResultState
import il.soulSalttrader.shabbattimes.model.toUnavailabilityWarning
import il.soulSalttrader.shabbattimes.network.NetworkResult
import il.soulSalttrader.shabbattimes.network.toBatchOutcome
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.UserPreferencesRepository
import il.soulSalttrader.shabbattimes.settings.OneTimeMessage
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.ShabbatEvent
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.shabbat.ShabbatUiState
import il.soulSalttrader.shabbattimes.useCase.GetHalachicTimesUseCase
import il.soulSalttrader.shabbattimes.useCase.ObserveGpsLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.RemoveSavedLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.ReorderLocationsUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    @param:InMemory private val currentLocationRepository: CurrentLocationRepository,
    @param:Persisted private val savedLocationsRepository: SavedLocationsRepository,
    private val reorderLocationsUseCase: ReorderLocationsUseCase,
    private val getHalachicTimesUseCase: GetHalachicTimesUseCase,
    private val removeLocationUseCase: RemoveSavedLocationUseCase,
    observeGpsLocationUseCase: ObserveGpsLocationUseCase,
    userPreferencesRepository: UserPreferencesRepository,
    permissionRepository: PermissionRepository,
) : ViewModel() {
    private val _effects: MutableSharedFlow<UiEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<UiEffect> = _effects.asSharedFlow()
    oneTimeMessageTracker: OneTimeMessageTracker,
) : BaseViewModel(oneTimeMessageTracker) {

    @OptIn(ExperimentalCoroutinesApi::class)
    val halachicTimesFlow: StateFlow<List<HalachicTimes>> = combine(
        currentLocationRepository.location,
        savedLocationsRepository.locations,
        userPreferencesRepository.shabbatPreferences,
    ) { gpsLocation, savedLocations, preferences ->
        val locations = buildList {
            gpsLocation?.let { add(it) }
            addAll(savedLocations)
        }

        locations to preferences
    }.flatMapLatest { (savedLocations, preferences) ->
        flow {
            val results = getHalachicTimesUseCase(savedLocations, preferences)
            val successes = results.filterIsInstance<NetworkResult.Success<HalachicTimes>>()
                .map { it.data }

            results.forEach { result ->
                when (result) {
                    is NetworkResult.Failure -> _effects.tryEmit(
                        UiEffect.ShowToast(result.cause.userMessage())
                    )
                    is NetworkResult.Success -> Unit
                }
            }
            successes.toUnavailabilityWarning()?.let { message ->
                emitOnce(OneTimeMessage.UNAVAILABLE_TIME_WARNING, UiEffect.ShowSnackBar(message))
            }
            emitBatchOutcome(results.toBatchOutcome())

            emit(successes)
        }
    }
        .catch { cause ->
            dispatch(ShabbatEvent.ShabbatEntryLoadFailed(cause))
            emitEffect(UiEffect.ShowToast(cause.userMessage()))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _state: MutableStateFlow<ShabbatUiState> = MutableStateFlow(value = ShabbatUiState())

    val state: StateFlow<ShabbatUiState> = combine(
        _state,
        halachicTimesFlow,
        observeGpsLocationUseCase(),
        savedLocationsRepository.locations,
        permissionRepository.permissionState,
    ) { state, halachicTimes, currentLocationState, savedLocations, permission ->
        ShabbatEvent.ShabbatEntryLoaded(savedLocations, currentLocationState, halachicTimes, permission).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ShabbatUiState(),
    )

    fun dispatch(event: UiEvent) {
        _state.updateAndGet { current ->
            when (event) {
                is ShabbatEvent -> event.reducer reduce current
                else            -> current
            }
        }

        when (event) {
            is ShabbatEvent.LocationDeleted  -> handleDeleteLocation(event)
            is ShabbatEvent.ReorderLocations -> handleReorderLocations(event)
            else                             -> Unit
        }
    }

    private fun handleReorderLocations(event: ShabbatEvent.ReorderLocations) {
        viewModelScope.launch {
            val entries = (state.value.shabbat as? ShabbatResultState.Ready)?.entries ?: return@launch
            reorderLocationsUseCase(entries, event.from, event.to)
        }
    }

    private fun handleDeleteLocation(event: ShabbatEvent.LocationDeleted) {
        viewModelScope.launch {
            removeLocationUseCase(event.savedLocation, event.isCurrent)
        }
    }
}