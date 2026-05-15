package il.soulSalttrader.shabbattimes.ui.shabbat

import android.Manifest
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresPermission
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.model.LocationStatus
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.model.ShabbatEntry
import il.soulSalttrader.shabbattimes.permission.HandlePermissions
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.permission.openAppSettings
import il.soulSalttrader.shabbattimes.ui.FailureScreen
import il.soulSalttrader.shabbattimes.ui.LoadingScreen
import il.soulSalttrader.shabbattimes.ui.effect.AppEffect
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent
import il.soulSalttrader.shabbattimes.ui.event.ShabbatEvent
import il.soulSalttrader.shabbattimes.ui.reorderable.SwipeConfig
import il.soulSalttrader.shabbattimes.ui.reorderable.SwipeState
import il.soulSalttrader.shabbattimes.ui.search.SearchConfig
import il.soulSalttrader.shabbattimes.ui.search.default
import il.soulSalttrader.shabbattimes.ui.search.toLocationStatus
import il.soulSalttrader.shabbattimes.ui.viewModel.PermissionViewModel
import il.soulSalttrader.shabbattimes.ui.viewModel.SearchViewModel
import il.soulSalttrader.shabbattimes.ui.viewModel.ShabbatViewModel
import kotlinx.collections.immutable.toImmutableList

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
@Composable
fun ShabbatScreen() {
    val shabbatViewModel: ShabbatViewModel = hiltViewModel()
    val shabbatState by shabbatViewModel.state.collectAsStateWithLifecycle()

    val searchViewModel: SearchViewModel = hiltViewModel()
    val searchUiState by searchViewModel.state.collectAsStateWithLifecycle()

    val permissionViewModel: PermissionViewModel = hiltViewModel()
    val permissionUiState by permissionViewModel.state.collectAsStateWithLifecycle()

    HandlePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ),
        permissionState = permissionUiState,
        dispatch = permissionViewModel::dispatch,
    )

    PermissionDialogs(
        permissionState = permissionUiState,
        dispatch = permissionViewModel::dispatch,
    )

    val context = LocalContext.current

    val onCardClick = {
        when (permissionUiState.permission) {
            PermissionState.Granted -> searchViewModel.dispatch(SearchEvent.GpsLocationRequested)
            else                    -> permissionViewModel.dispatch(PermissionEvent.ShowEducation)
        }
    }

    val searchConfig = SearchConfig(
        state = searchUiState.default(),
        action = searchViewModel.default(),
    )

    when (val entries = shabbatState.shabbat) {
        is ShabbatResultState.Idle      -> LoadingScreen()

        is ShabbatResultState.Loading   -> LoadingScreen()

        is ShabbatResultState.Empty -> {
            ShabbatContent(
                items = listOf(
                    ShabbatEntry(
                        location = SavedLocation.empty(),
                        times = null,
                        status = searchUiState.gpsResult.toLocationStatus(),
                    ),
                ).toImmutableList(),
                isDraggable = false,
                searchConfig = searchConfig,
                onClick = onCardClick,
            )
        }

        is ShabbatResultState.Ready -> {
            ShabbatContent(
                items = entries.entries,
                swipeConfig = SwipeConfig(toLeft = SwipeState.Delete) { item ->
                    shabbatViewModel.dispatch(
                        ShabbatEvent.LocationDeleted(
                            savedLocation = item.location,
                            isCurrent = item.status == LocationStatus.Current,
                        )
                    )
                },
                searchConfig = searchConfig,
                onClick = onCardClick,
                onReorder = { from, to ->
                    shabbatViewModel.dispatch(
                        ShabbatEvent.ReorderLocations(from = from, to = to,)
                    )
                },
            )
        }

        is ShabbatResultState.Failure   -> FailureScreen(
            message = entries.message,
            onRetry = { shabbatViewModel.dispatch(ShabbatEvent.RetryLoadShabbatEntry) },
        )
    }

    LaunchedEffect(Unit) {
        searchViewModel.effects.collect { effect ->
            when (effect) {
                is AppEffect.ShowToast -> {
                    if (Debug.enabled) {
                        Log.d("ShabbatScreen", "Toast: $effect ${effect.message}")
                    }
                    Toast.makeText(context, effect.message, Toast.LENGTH_SHORT).show()
                }

                else                   -> Unit
            }
        }
    }

    LaunchedEffect(Unit) {
        permissionViewModel.effects.collect { effect ->
            when (effect) {
                is AppEffect.OpenAppSettings -> {
                    if (Debug.enabled) {
                        Log.d("ShabbatScreen", "OpenAppSettings: $permissionUiState")
                    }
                    context.openAppSettings()
                }

                else                         -> Unit
            }
        }
    }
}