package il.soulSalttrader.shabbattimes.ui.permission

import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.shabbat.CardAction

fun PermissionUiState.dispatchCardAction(): CardAction = when (permission) {
        PermissionState.Granted           -> CardAction.OpenGpsSearch
        PermissionState.Denied            -> CardAction.AcceptRationale
        PermissionState.DeniedPermanently -> CardAction.ShowDeniedDialog
        else                              -> CardAction.ShowEducation
}