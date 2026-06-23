package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.shabbat.CardAction

fun PermissionState.dispatchCardAction(): CardAction = when (this) {
        PermissionState.Granted                    -> CardAction.OpenGpsSearch
        PermissionState.Denied                     -> CardAction.ShowDeniedRationale
        PermissionState.DeniedPermanently          -> CardAction.ShowDeniedPermanently
        PermissionState.Requesting,
        PermissionState.DeniedRationale,
        PermissionState.DeniedPermanentlyRationale -> CardAction.None
        PermissionState.Idle,
        PermissionState.Education                  -> CardAction.ShowEducation
}