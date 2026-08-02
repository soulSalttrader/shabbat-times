package il.soulSalttrader.shabbattimes.ui.event

import il.soulSalttrader.shabbattimes.settings.OneTimeMessage.LOCATION_PERMISSION_EDUCATION
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker

internal suspend fun PermissionEvent.resolve(tracker: OneTimeMessageTracker): PermissionEvent =
    when (this) {
        is PermissionEvent.PermissionRequested ->
            if (tracker.hasShown(LOCATION_PERMISSION_EDUCATION)) {
                PermissionEvent.RequestPermission
            } else {
                PermissionEvent.ShowEducation
            }
        else -> this
    }
