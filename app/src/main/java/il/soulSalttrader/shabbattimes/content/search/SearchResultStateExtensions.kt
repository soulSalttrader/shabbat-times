package il.soulSalttrader.shabbattimes.content.search

import il.soulSalttrader.shabbattimes.location.LocationStatus

fun SearchResultState.toLocationStatus() = when (this) {
    is SearchResultState.GpsLocation  -> LocationStatus.Current
    is SearchResultState.Requesting   -> LocationStatus.Locating
    is SearchResultState.NoPermission -> LocationStatus.NoPermission
    is SearchResultState.NoResults    -> LocationStatus.Unknown
    is SearchResultState.Idle         -> LocationStatus.Unknown
    is SearchResultState.Failure      -> LocationStatus.Unknown
    else                              -> LocationStatus.Unknown
}