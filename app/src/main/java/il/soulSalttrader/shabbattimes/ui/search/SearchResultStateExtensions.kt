package il.soulSalttrader.shabbattimes.ui.search

import il.soulSalttrader.shabbattimes.location.LocationStatus

fun SearchResultState.toLocationStatus() = when (this) {
    is SearchResultState.GpsResolved -> LocationStatus.Current
    is SearchResultState.Loading     -> LocationStatus.Locating
    else                             -> LocationStatus.Unknown
}