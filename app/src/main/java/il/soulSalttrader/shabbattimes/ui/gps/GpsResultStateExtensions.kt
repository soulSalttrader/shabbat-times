package il.soulSalttrader.shabbattimes.ui.gps

import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.permission.PermissionState

fun GpsResultState.toLocationStatus(permission: PermissionState) = when {
    permission is PermissionState.Denied                     -> LocationStatus.NoPermission
    permission is PermissionState.DeniedPermanently          -> LocationStatus.NoPermission
    permission is PermissionState.DeniedRationale            -> LocationStatus.NoPermission
    permission is PermissionState.DeniedPermanentlyRationale -> LocationStatus.NoPermission
    permission is PermissionState.Requesting                 -> LocationStatus.Locating
    this is GpsResultState.Resolved                          -> LocationStatus.Current
    this is GpsResultState.Loading                           -> LocationStatus.Locating
    else                                                     -> LocationStatus.Unknown
}