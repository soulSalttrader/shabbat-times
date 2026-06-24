package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.permission.PermissionState

fun SearchResultState.toLocationStatus(permission: PermissionState) = when {
    permission is PermissionState.Denied                     -> LocationStatus.NoPermission
    permission is PermissionState.DeniedPermanently          -> LocationStatus.NoPermission
    permission is PermissionState.DeniedRationale            -> LocationStatus.NoPermission
    permission is PermissionState.DeniedPermanentlyRationale -> LocationStatus.NoPermission
    permission is PermissionState.Requesting                 -> LocationStatus.Locating
    this is SearchResultState.GpsResolved                    -> LocationStatus.Current
    this is SearchResultState.Loading                        -> LocationStatus.Locating
    else                                                     -> LocationStatus.Unknown
}