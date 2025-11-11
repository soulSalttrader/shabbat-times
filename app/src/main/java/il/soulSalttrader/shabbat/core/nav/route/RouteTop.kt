package il.soulSalttrader.retro.core.nav.route

sealed interface RouteTop : Route {
    object PreviousScreen : RouteTop
    object HistoryScreen : RouteTop
    object FavoriteScreen : RouteTop
}