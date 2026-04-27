package il.soulSalttrader.shabbattimes.location

sealed interface LocationPermission {
    data object Idle : LocationPermission
    data object Education : LocationPermission
    data object Requesting : LocationPermission
    data object Granted : LocationPermission
    data object Denied : LocationPermission
    data object DeniedPermanently : LocationPermission
}