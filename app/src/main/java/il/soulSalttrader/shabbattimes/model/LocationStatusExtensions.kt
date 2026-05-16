package il.soulSalttrader.shabbattimes.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.common.roundToDecimalPlaces

@Composable
fun LocationStatus.toLabel(): String = when (this) {
    is LocationStatus.Current           -> stringResource(R.string.location_status_current)
    is LocationStatus.Nearby            -> stringResource(R.string.location_status_nearby, distanceKm.roundToDecimalPlaces(1))
    is LocationStatus.Locating          -> stringResource(R.string.location_status_locating)
    is LocationStatus.Unknown           -> stringResource(R.string.location_status_unknown)
    is LocationStatus.NoPermission      -> stringResource(R.string.location_status_no_permission)
    is LocationStatus.LastKnownLocation -> stringResource(R.string.location_status_last_known)
}