package il.soulSalttrader.shabbattimes.model

import il.soulSalttrader.shabbattimes.permission.PermissionState

fun LocationPermission.toPermissionState() = when (this) {
    is LocationPermission.Idle                       -> PermissionState.Idle
    is LocationPermission.Education                  -> PermissionState.Education
    is LocationPermission.Requesting                 -> PermissionState.Requesting
    is LocationPermission.Granted                    -> PermissionState.Granted
    is LocationPermission.Denied                     -> PermissionState.Denied
    is LocationPermission.DeniedPermanently          -> PermissionState.DeniedPermanently
    is LocationPermission.DeniedPermanentlyRationale -> PermissionState.DeniedPermanentlyRationale
    is LocationPermission.DeniedRationale            -> PermissionState.DeniedRationale
}