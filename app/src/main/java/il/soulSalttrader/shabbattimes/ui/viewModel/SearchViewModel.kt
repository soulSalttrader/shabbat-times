package il.soulSalttrader.shabbattimes.ui.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.common.constants.LocationConfig.MAX_SAVED_LOCATIONS
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SaveLocationResult
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.effect.AppEffect
import il.soulSalttrader.shabbattimes.ui.event.AppEvent
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent
import il.soulSalttrader.shabbattimes.ui.normalizedOrEmpty
import il.soulSalttrader.shabbattimes.ui.normalizedOrNull
import il.soulSalttrader.shabbattimes.ui.search.SearchUiState
import il.soulSalttrader.shabbattimes.useCase.GetLocationSuggestionsUseCase
import il.soulSalttrader.shabbattimes.useCase.ResolveGpsLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.SaveLocationUseCase
import il.soulSalttrader.shabbattimes.useCase.UpdateCurrentLocationUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val saveLocationUseCase: SaveLocationUseCase,
    private val updateCurrentLocationUseCase: UpdateCurrentLocationUseCase,
    private val getLocationSuggestion: GetLocationSuggestionsUseCase,
    resolveGpsLocationUseCase: ResolveGpsLocationUseCase,
    permissionRepository: PermissionRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(value = SearchUiState())

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val queryFlow: Flow<String> = _state
        .map { it.query.normalizedOrEmpty() }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val suggestionsLocationFlow: StateFlow<List<ResolvedLocation>> = queryFlow
        .debounce(300)
        .flatMapLatest { query ->
            flow {
                getLocationSuggestion(query)
                    .onSuccess("SearchVM") { suggestions -> emit(suggestions) }
                    .onFailure("SearchVM") { e -> _effects.tryEmit(AppEffect.ShowToast(UiText.Resource(R.string.error_unknown))) }
            }
        }
        .catch { cause ->
            if (Debug.enabled) Log.e("ShabbatViewModel", "Unexpected error", cause)
            SearchEvent.SuggestionsLoadFailed(cause).reducer reduce _state.value
            _effects.tryEmit(AppEffect.ShowToast(UiText.Resource(R.string.error_unexpected)))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    @OptIn(ExperimentalCoroutinesApi::class)
    private val gpsLocationFlow: StateFlow<ResolvedLocation?> = resolveGpsLocationUseCase()
        .onStart { dispatch(SearchEvent.GpsLocationRequested) }
        .onEach { resolved -> updateCurrentLocationUseCase(resolved) }
        .catch { cause ->
            if (Debug.enabled) Log.e("SearchViewModel", "GPS error", cause)
            dispatch(SearchEvent.GpsLocationError(cause))
            _effects.tryEmit(AppEffect.ShowToast(UiText.Resource(R.string.error_unknown)))
            emit(null)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = null,
        )

    val state: StateFlow<SearchUiState> = combine(
        _state,
        suggestionsLocationFlow,
        permissionRepository.permissionState,
        gpsLocationFlow,
    ) { state, suggestions, permission, gpsLocation ->
        val withGpsLocation = gpsLocation?.let { SearchEvent.GpsLocationLoaded(it).reducer reduce state } ?: state
        val withPermission = SearchEvent.GpsPermissionChanged(permission).reducer reduce withGpsLocation

        SearchEvent.SuggestionsLoaded(suggestions).reducer reduce withPermission
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is SearchEvent -> event.reducer reduce current
                else           -> current
            }
        }

        when (event) {
            is SearchEvent.SuggestionSelected -> handleSuggestionSelected(newState)
            else                              -> Unit
        }
    }

    private fun handleSuggestionSelected(state: SearchUiState) {
        val resolved = state.selectedSuggestion.normalizedOrNull() ?: return

        viewModelScope.launch {
            when (saveLocationUseCase(resolved)) {
                SaveLocationResult.LimitReached -> _effects.tryEmit(
                    AppEffect.ShowSnackBar(
                        message = UiText.Resource(
                            id = R.string.search_limit_reached,
                            args = listOf(MAX_SAVED_LOCATIONS),
                        ),
                        actionLabel = UiText.Resource(R.string.search_limit_action),
                        onAction = { dispatch(SearchEvent.SearchVisibilityChanged(false)) },
                    )
                )
                SaveLocationResult.Success -> Unit
            }
            saveLocationUseCase(resolved)
        }
    }
}