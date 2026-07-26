package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.common.constants.LocationConfig.MAX_SAVED_LOCATIONS
import il.soulSalttrader.shabbattimes.common.userMessage
import il.soulSalttrader.shabbattimes.model.ResolvedLocation
import il.soulSalttrader.shabbattimes.model.SaveLocationResult
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.normalizedOrEmpty
import il.soulSalttrader.shabbattimes.ui.normalizedOrNull
import il.soulSalttrader.shabbattimes.ui.search.SearchUiState
import il.soulSalttrader.shabbattimes.useCase.GetLocationSuggestionsUseCase
import il.soulSalttrader.shabbattimes.useCase.SaveLocationUseCase
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val saveLocationUseCase: SaveLocationUseCase,
    private val getLocationSuggestion: GetLocationSuggestionsUseCase,
    oneTimeMessageTracker: OneTimeMessageTracker,
) : BaseViewModel(oneTimeMessageTracker) {
    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(value = SearchUiState())
    private val queryFlow: Flow<String> = _state
        .map { it.query.normalizedOrEmpty() }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val suggestionsLocationFlow: StateFlow<List<ResolvedLocation>> = queryFlow
        .debounce(300)
        .flatMapLatest { query ->
            flow {
                getLocationSuggestion(query)
                    .onSuccess { suggestions -> emit(suggestions) }
                    .onFailure { e -> emitEffect(UiEffect.ShowToast(e.cause.userMessage())) }
            }
        }
        .catch { cause ->
            SearchEvent.SuggestionsLoadFailed(cause).reducer reduce _state.value
            emitEffect(UiEffect.ShowToast(cause.userMessage()))
            emit(emptyList())
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val state: StateFlow<SearchUiState> = combine(
        _state,
        suggestionsLocationFlow,
    ) { state, suggestions ->
        SearchEvent.SuggestionsLoaded(suggestions).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun dispatch(event: UiEvent) {
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
                SaveLocationResult.LimitReached -> emitEffect(
                    UiEffect.ShowSnackBar(
                        message = UiText.Resource(
                            id = R.string.search_limit_reached,
                            args = listOf(MAX_SAVED_LOCATIONS),
                        ),
                        actionLabel = UiText.Resource(R.string.search_limit_action),
                        onAction = { dispatch(SearchEvent.SearchVisibilityChanged(false)) },
                    )
                )
                SaveLocationResult.Success -> emitEffect(UiEffect.ShowToast(UiText.Resource(R.string.location_added)))
            }
        }
    }
}