package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.ui.effect.AppEffect
import il.soulSalttrader.shabbattimes.ui.event.AppEvent
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@HiltViewModel
class PermissionViewModel @Inject constructor(
    private val permissionRepository: PermissionRepository,
): ViewModel() {
    private val _effects: MutableSharedFlow<AppEffect> = MutableSharedFlow(extraBufferCapacity = 20)
    val effects: SharedFlow<AppEffect> = _effects.asSharedFlow()

    private val _state: MutableStateFlow<PermissionUiState> = MutableStateFlow(PermissionUiState())
    val state: StateFlow<PermissionUiState> = combine(
        _state,
        permissionRepository.permissionState,
    ) { state, permission ->
        PermissionEvent.PermissionChanged(permission).reducer reduce state
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PermissionUiState()
    )

    fun dispatch(event: AppEvent) {
        _state.update { current ->
            when (event) {
                is PermissionEvent  -> event.reducer reduce current
                else                -> current
            }
        }

        when (event) {
            is PermissionEvent.AllGranted               -> permissionRepository.updatePermissionState(LocationPermission.Granted)
            is PermissionEvent.DeniedPermanently        -> permissionRepository.updatePermissionState(LocationPermission.DeniedPermanently)
            is PermissionEvent.DeniedWithRationale      -> permissionRepository.updatePermissionState(LocationPermission.Denied)
            is PermissionEvent.ShowEducation            -> permissionRepository.updatePermissionState(LocationPermission.Education)
            is PermissionEvent.Request                  -> permissionRepository.updatePermissionState(LocationPermission.Requesting)
            is PermissionEvent.AcceptedRationale        -> permissionRepository.updatePermissionState(LocationPermission.Requesting)
            is PermissionEvent.ReturnedFromAppSettings  -> permissionRepository.updatePermissionState(LocationPermission.Requesting)
            is PermissionEvent.RequestedAppSettings     -> _effects.tryEmit(AppEffect.OpenAppSettings)

            else -> Unit
        }
    }
}