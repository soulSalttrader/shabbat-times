package il.soulSalttrader.shabbattimes.ui.effect

import dagger.hilt.android.scopes.ViewModelScoped
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.settings.OneTimeMessage
import il.soulSalttrader.shabbattimes.settings.OneTimeMessage.LOCATION_PERMISSION_EDUCATION
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import javax.inject.Inject

@ViewModelScoped
class PermissionSideEffectHandler @Inject constructor(
    private val permissionRepository: PermissionRepository,
    private val oneTimeMessageTracker: OneTimeMessageTracker,
) : SideEffectHandler<PermissionEvent> {

    override suspend fun handle(event: PermissionEvent, emitter: EffectEmitter) {
        when (event) {
            is PermissionEvent.SystemGranted           -> permissionRepository.updatePermissionState(LocationPermission.Granted)
            is PermissionEvent.SystemDeniedPermanently -> permissionRepository.updatePermissionState(LocationPermission.DeniedPermanently)
            is PermissionEvent.SystemDenied            -> permissionRepository.updatePermissionState(LocationPermission.DeniedRationale)
            is PermissionEvent.ReturnedFromAppSettings  -> permissionRepository.updatePermissionState(LocationPermission.Idle)
            is PermissionEvent.ShowEducation -> {
                val alreadyEducated = oneTimeMessageTracker.hasShown(LOCATION_PERMISSION_EDUCATION)
                when (alreadyEducated) {
                    true -> {
                        permissionRepository.updatePermissionState(LocationPermission.Requesting)
                        oneTimeMessageTracker.markShown(OneTimeMessage.LOCATION_PERMISSION_REQUESTED)
                    }
                    else -> {
                        permissionRepository.updatePermissionState(LocationPermission.Education)
                        oneTimeMessageTracker.markShown(LOCATION_PERMISSION_EDUCATION)
                    }
                }
            }

            is PermissionEvent.Request -> {
                permissionRepository.updatePermissionState(LocationPermission.Requesting)
                oneTimeMessageTracker.markShown(OneTimeMessage.LOCATION_PERMISSION_REQUESTED)
            }
            is PermissionEvent.OpenAppSettings          -> emitter.emitEffect(UiEffect.OpenAppSettings)

            else -> {} // TODO: Integrate logging framework (Timber)
        }
    }
}