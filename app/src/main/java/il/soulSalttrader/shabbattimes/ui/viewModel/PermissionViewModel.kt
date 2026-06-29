package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.model.toPermissionState
import il.soulSalttrader.shabbattimes.model.toUiState
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
        permissionRepository.permissionState.value.toUiState(PermissionUiState())
    )

    val state: StateFlow<PermissionUiState> = _state.asStateFlow()

    init {
        permissionRepository.permissionState
            .onEach { permission ->
                _state.update { current ->
                    current.copy(permission = permission.toPermissionState())
                }
            }
            .launchIn(viewModelScope)
    }

    fun dispatch(event: UiEvent) {
        onDispatch(event)
        _state.update { current ->
            when (event) {
                is PermissionEvent.DismissEducation,
                is PermissionEvent.TappedCardDenied,
                is PermissionEvent.TappedCardDeniedPermanently,
                is PermissionEvent.DismissDeniedRationale,
                is PermissionEvent.DismissDeniedPermanently -> event.reducer reduce current
                else                                        -> current
            }
        }

        if (event is PermissionEvent) {
            viewModelScope.launch {
                permissionSideEffectHandler.handle(event, this@PermissionViewModel)
            }
        }
    }
}