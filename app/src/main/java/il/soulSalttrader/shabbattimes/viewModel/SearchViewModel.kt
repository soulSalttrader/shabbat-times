package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.content.search.SearchUiState
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.network.onFailure
import il.soulSalttrader.shabbattimes.network.onSuccess
import il.soulSalttrader.shabbattimes.repository.CityRepository
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
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CityRepository,
) : ViewModel() {
    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(value = SearchUiState())

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val queryFlow: Flow<String> = _state
        .map { it.query.trim() }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val suggestionsFlow: StateFlow<List<City>> = queryFlow
        .debounce(300)
        .flatMapLatest { query ->
            query.takeUnless { it.isEmpty() }
                ?.let { nonEmptyQuery ->
                    flow {
                        val result = repository.geocodeAutocomplete(nonEmptyQuery)

                        result
                            .onSuccess(tag = "SearchVM") { suggestions ->
                                SearchEvent.SuggestionsLoaded(cities = suggestions)
                                emit(suggestions)
                            }
                            .onFailure(tag = "SearchVM") { e ->
                                SearchEvent.SuggestionsLoadFailed(message = e.message, cause = e.cause)
                            }
                    }
                } ?: flowOf(emptyList())

        }
        .catch { emit(emptyList()) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val state: StateFlow<SearchUiState> = combine(
        _state,
        suggestionsFlow,
    ) { state, suggestions ->
        SearchEvent.SuggestionsLoaded(suggestions).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SearchUiState()
    )

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is SearchEvent -> event.reducer reduce current
                else -> current
            }
        }

        when (event) {
            is SearchEvent.SuggestionSelected -> handleSuggestionSelected(newState)
            else -> Unit
        }
    }

    private fun handleSuggestionSelected(state: SearchUiState) {
        val city = state.selectedSuggestion ?: return

        viewModelScope.launch {
            repository.addCity(city)
        }
    }
}