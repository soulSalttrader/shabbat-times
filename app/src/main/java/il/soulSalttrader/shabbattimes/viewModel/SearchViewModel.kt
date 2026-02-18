package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.model.City
import il.soulSalttrader.shabbattimes.model.SearchUiState
import il.soulSalttrader.shabbattimes.repository.CityRepository
import jakarta.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.updateAndGet
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: CityRepository,
) : ViewModel() {

    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(value = SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val queryFlow = _state
        .map { it.query.trim() }
        .distinctUntilChanged()

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    private val searchResultsFlow: Flow<List<City>> = queryFlow
        .debounce(300)
        .flatMapLatest { query ->
            when {
                query.length < 2 -> flowOf(emptyList())
                else -> repository.geocodeAutocomplete(query)
            }.catch { emit(emptyList()) }
        }

    init {
        observeSearchResults()
    }

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

    private fun observeSearchResults() {
        viewModelScope.launch {
            searchResultsFlow.collectLatest { cities ->
                dispatch(SearchEvent.SuggestionsLoaded(cities))
            }
        }
    }
}