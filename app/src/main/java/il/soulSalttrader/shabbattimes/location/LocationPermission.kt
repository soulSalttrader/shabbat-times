package il.soulSalttrader.shabbattimes.location

sealed interface LocationPermission {
    data object Granted : LocationPermission
    data object Denied : LocationPermission
}