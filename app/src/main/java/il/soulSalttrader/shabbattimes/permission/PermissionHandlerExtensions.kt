package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

fun PermissionHandler.resolvePermissionEvent(permissions: List<String>): PermissionEvent? {
    val allGranted = permissions.all { isGranted(it) }
    val anyPermanentlyDenied = permissions.any { !isGranted(it) && !shouldShowRationale(it) }
    val anyDenied = permissions.any { !isGranted(it) && shouldShowRationale(it) }

    return when {
        allGranted           -> PermissionEvent.AllGranted
        anyPermanentlyDenied -> PermissionEvent.DeniedPermanently
        anyDenied            -> PermissionEvent.DeniedWithRationale
        else                 -> null
    }
}