package il.soulSalttrader.shabbattimes.model

sealed interface LocationPermission {
    data object Idle : LocationPermission
    data object Education : LocationPermission
    data object Requesting : LocationPermission
    data object Granted : LocationPermission
    data object Denied : LocationPermission
    data object DeniedPermanently : LocationPermission
}