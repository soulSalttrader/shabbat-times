package il.soulSalttrader.retro.shabbatApp.content

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.core.Debug
import il.soulSalttrader.retro.core.effect.AppEffect
import il.soulSalttrader.retro.core.event.PermissionEvent
import il.soulSalttrader.retro.core.event.ShabbatDataEvent
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState
import il.soulSalttrader.retro.shabbatApp.permission.HandlePermissions
import il.soulSalttrader.retro.shabbatApp.permission.openAppSettings
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatViewModel

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
        is ShabbatDataState.Idle  -> LoadingScreen()

        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            result = (state.data as ShabbatDataState.Success).data ?: HalachicTimesDisplay(),
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