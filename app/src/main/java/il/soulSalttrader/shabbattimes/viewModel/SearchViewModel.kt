package il.soulSalttrader.shabbattimes.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.AppEvent
import il.soulSalttrader.shabbattimes.event.SearchEvent
import il.soulSalttrader.shabbattimes.model.SearchUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.updateAndGet

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _state: MutableStateFlow<SearchUiState> = MutableStateFlow(value = SearchUiState())
    val state: StateFlow<SearchUiState> = _state.asStateFlow()

    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    fun dispatch(event: AppEvent) {
        val newState = _state.updateAndGet { current ->
            when (event) {
                is SearchEvent -> event.reducer reduce current
                else -> current
            }
        }

        when (event) {
            is SearchEvent.SuggestionSelected -> TODO()
            else -> Unit
        }
    }
}