package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.effect.SideEffectHandler
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class PermissionViewModel @Inject constructor(
    val permissionSideEffectHandler: SideEffectHandler<PermissionEvent>,
    permissionRepository: PermissionRepository,
    oneTimeMessageTracker: OneTimeMessageTracker,
): BaseViewModel(oneTimeMessageTracker) {
    internal var onDispatch: (UiEvent) -> Unit = {}

    private val _state: MutableStateFlow<PermissionUiState> = MutableStateFlow(
        PermissionEvent.PermissionChanged(
            permissionRepository.permissionState.value
        ).reducer reduce PermissionUiState()
    )

    val state: StateFlow<PermissionUiState> = _state.asStateFlow()

    init {
        permissionRepository.permissionState
            .onEach { permission ->
                _state.update { current ->
                    PermissionEvent.PermissionChanged(permission).reducer reduce current
                }
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(event: UiEvent) {
        onDispatch(event)
        _state.update { current ->
            when (event) {
                is PermissionEvent  -> event.reducer reduce current
                else                -> current
            }
        }

        if (event is PermissionEvent) {
            viewModelScope.launch {
                permissionSideEffectHandler.handle(event, this@PermissionViewModel )
            }
        }
    }
}