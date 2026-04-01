package il.soulSalttrader.shabbattimes.content.shabbat

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.FailureScreen
import il.soulSalttrader.shabbattimes.content.LoadingScreen
import il.soulSalttrader.shabbattimes.content.reorderable.SwipeConfig
import il.soulSalttrader.shabbattimes.content.reorderable.SwipeState
import il.soulSalttrader.shabbattimes.content.search.SearchConfig
import il.soulSalttrader.shabbattimes.content.search.default
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.PermissionEvent
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
import il.soulSalttrader.shabbattimes.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.permission.HandlePermissions
import il.soulSalttrader.shabbattimes.permission.openAppSettings
import il.soulSalttrader.shabbattimes.viewModel.SearchViewModel
import il.soulSalttrader.shabbattimes.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val shabbatViewModel: ShabbatViewModel = hiltViewModel()
    val shabbatState by shabbatViewModel.state.collectAsStateWithLifecycle()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchUiState by searchViewModel.state.collectAsStateWithLifecycle()

    HandlePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        permissionState = shabbatState.permission,
        dispatch = shabbatViewModel::dispatch,
    )

    val context = LocalContext.current

    when (val halachicTimes = shabbatState.data) {
        is ShabbatResultState.Idle      -> LoadingScreen()

        is ShabbatResultState.Loading   -> LoadingScreen()

        is ShabbatResultState.NoResults -> {
            ShabbatContent(
                items = listOf(HalachicTimesDisplay()),
                isDraggable = false,
                searchConfig = SearchConfig(
                    state = searchUiState.default(),
                    action = searchViewModel.default(),
                ),

                onClick = { shabbatViewModel.dispatch(PermissionEvent.Request)},
            )
        }

        is ShabbatResultState.Results   -> {
            ShabbatContent(
                items = halachicTimes.data,
                swipeConfig = SwipeConfig(toLeft = SwipeState.Delete) {
                    shabbatViewModel.dispatch(ShabbatDataEvent.TimeDeleted(it.city))
                },
                searchConfig = SearchConfig(
                    state = searchUiState.default(),
                    action = searchViewModel.default(),
                ),
            )
        }

        is ShabbatResultState.Failure   -> FailureScreen(
            message = halachicTimes.message,
            onRetry = { shabbatViewModel.dispatch(ShabbatDataEvent.RetryLoadTimes) },
        )
    }

    LaunchedEffect(Unit) {
        searchViewModel.effects.collect { effect ->
            when (effect) {
                is AppEffect.ShowToast -> {
                    if (Debug.enabled) { Log.d("ShabbatScreen", "Toast: $effect ${effect.message}") }
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                else -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        shabbatViewModel.effects.collect { effect ->
            when (effect) {
                is AppEffect.OpenAppSettings -> {
                    if (Debug.enabled) { Log.d("ShabbatScreen", "OpenAppSettings: $shabbatState") }
                    context.openAppSettings()
                }

                else -> Unit
            }
        }
    }
}