package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

fun PermissionHandler.resolvePermissionEvent(permissions: List<String>): PermissionEvent? {
    val allGranted = permissions.all { isGranted(it) }
    if (allGranted) { return PermissionEvent.SystemGranted }

    val anyNeedsRationale = permissions.any { !isGranted(it) && shouldShowRationale(it) }

    return when {
        anyNeedsRationale -> PermissionEvent.SystemDenied
        else -> null
    }
}