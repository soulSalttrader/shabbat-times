package il.soulSalttrader.retro.shabbatApp.viewModel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimes
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.model.toDisplay
import il.soulSalttrader.retro.shabbatApp.network.NetworkResult
import il.soulSalttrader.retro.shabbatApp.repository.ShabbatRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ShabbatViewModel @Inject constructor(
    private val repository: ShabbatRepository,
    @param:ApplicationContext private val context: Context,
) : ViewModel() {
    private val _uiState: MutableStateFlow<ShabbatUiState> =
        MutableStateFlow(ShabbatUiState.Loading)
    val uiState: StateFlow<ShabbatUiState> = _uiState.asStateFlow()

    init {
        dispatch(event = ShabbatEvent.Load)
    }

    fun dispatch(event: ShabbatEvent) {
        if (event is ShabbatEvent.Load) {
            loadData()
        }

        _uiState.update { current -> event.reducer reduce current }
    }

    private fun loadData() {
        viewModelScope.launch {
            val result = repository.getHalachicTimes()
            val displayData = result.toDisplayData()
            val event = result.toLoadedEvent(displayData)

            dispatch(event)
        }
    }

    private fun NetworkResult<HalachicTimes>.toDisplayData() = when (this) {
        is NetworkResult.Success -> data.toDisplay(context)
        is NetworkResult.Failure -> null
    }

    private fun NetworkResult<HalachicTimes>.toLoadedEvent(
        display: HalachicTimesDisplay?
    ) = when (this) {
        is NetworkResult.Success -> ShabbatEvent.Loaded.Success(display)
        is NetworkResult.Failure -> ShabbatEvent.Loaded.Failure(message = message, cause = cause)
    }
}