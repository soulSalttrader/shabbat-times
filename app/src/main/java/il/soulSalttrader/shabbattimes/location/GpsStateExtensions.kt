package il.soulSalttrader.shabbattimes.location

fun GpsState.toLocationStatus() = when (this) {
    is GpsState.Ready        -> LocationStatus.Current
    is GpsState.Loading      -> LocationStatus.Locating
    is GpsState.NoPermission -> LocationStatus.NoPermission
    is GpsState.Idle         -> LocationStatus.Unknown
    is GpsState.Error        -> LocationStatus.Unknown
}