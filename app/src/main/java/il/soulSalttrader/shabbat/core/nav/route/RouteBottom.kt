package il.soulSalttrader.retro.core.nav.route

sealed interface RouteBottom : Route {
    object HomeScreen : RouteBottom
    object AlertsScreen : RouteBottom
    object SettingsScreen : RouteBottom
//    object MoreScreen : RouteBottom
    object BreatheScreen : RouteBottom
    object ShabbatScreen : RouteBottom
}