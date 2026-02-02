package il.soulSalttrader.shabbattimes.content

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.model.ShabbatDataState
import il.soulSalttrader.shabbattimes.permission.HandlePermissions
import il.soulSalttrader.shabbattimes.permission.openAppSettings
import il.soulSalttrader.shabbattimes.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val viewModel: ShabbatViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    HandlePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        permissionState = state.permission,
        dispatch = viewModel::dispatch,
    )

    val context = LocalContext.current

    when (state.data) {
        is ShabbatDataState.Idle    -> LoadingScreen()

        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            times = (state.data as ShabbatDataState.Success).data,
            onClick = { viewModel.dispatch(PermissionEvent.Request) },
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (state.data as ShabbatDataState.Failure).message,
            onRetry = { viewModel.dispatch(ShabbatDataEvent.Load) },
        )
    }

    LaunchedEffect(state.permission) {
        if (Debug.enabled) { Log.d("ShabbatScreen", "$state") }
        viewModel.effects.collect { effect ->
            if (Debug.enabled) { Log.d("ShabbatScreen", "$effect") }

            when (effect) {
                is AppEffect.Shabbat.OpenAppSettings -> context.openAppSettings()
                else -> Unit
            }
        }
    }
}