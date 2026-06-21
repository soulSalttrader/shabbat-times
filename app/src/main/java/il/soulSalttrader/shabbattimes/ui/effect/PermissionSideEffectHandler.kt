package il.soulSalttrader.shabbattimes.ui.effect

import dagger.hilt.android.scopes.ViewModelScoped
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import javax.inject.Inject

@ViewModelScoped
class PermissionSideEffectHandler @Inject constructor(
    private val permissionRepository: PermissionRepository,
) : SideEffectHandler<PermissionEvent> {

    override fun handle(event: PermissionEvent, emitter: EffectEmitter) {
        when (event) {
            is PermissionEvent.AllGranted               -> permissionRepository.updatePermissionState(LocationPermission.Granted)
            is PermissionEvent.DeniedPermanently        -> permissionRepository.updatePermissionState(LocationPermission.DeniedPermanently)
            is PermissionEvent.DeniedWithRationale      -> permissionRepository.updatePermissionState(LocationPermission.Denied)
            is PermissionEvent.ShowEducation            -> permissionRepository.updatePermissionState(LocationPermission.Education)
            is PermissionEvent.Request                  -> permissionRepository.updatePermissionState(LocationPermission.Requesting)
            is PermissionEvent.AcceptedRationale        -> permissionRepository.updatePermissionState(LocationPermission.Requesting)
            is PermissionEvent.ReturnedFromAppSettings  -> permissionRepository.updatePermissionState(LocationPermission.Idle)
            is PermissionEvent.RequestedAppSettings     -> emitter.emitEffect(UiEffect.OpenAppSettings)

            else -> {} // TODO: Integrate logging framework (Timber)
        }
    }
}