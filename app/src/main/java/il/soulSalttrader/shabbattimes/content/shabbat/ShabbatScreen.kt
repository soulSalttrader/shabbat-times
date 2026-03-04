package il.soulSalttrader.shabbattimes.content.shabbat

import android.Manifest
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.Debug
import il.soulSalttrader.shabbattimes.content.FailureScreen
import il.soulSalttrader.shabbattimes.content.LoadingScreen
import il.soulSalttrader.shabbattimes.content.search.hasQuery
import il.soulSalttrader.shabbattimes.content.search.isSearchActive
import il.soulSalttrader.shabbattimes.content.search.suggestionsOrEmpty
import il.soulSalttrader.shabbattimes.effect.AppEffect
import il.soulSalttrader.shabbattimes.event.ShabbatDataEvent
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

        is ShabbatResultState.NoResults -> LoadingScreen()

        is ShabbatResultState.Results   -> {
            val suggestions = searchUiState.suggestionsOrEmpty()
            val searchActive  = searchUiState.isSearchActive()
            val hasQuery = searchUiState.hasQuery()

            ShabbatContent(
                halachicTimesDisplay = halachicTimes.data,
                shabbatDispatch = shabbatViewModel::dispatch,

                suggestions = suggestions,
                hasQuery = hasQuery,
                searchActive = searchActive,
                searchDispatch = searchViewModel::dispatch,
            )
        }

        is ShabbatResultState.Failure   -> FailureScreen(
            message = halachicTimes.message,
            onRetry = { shabbatViewModel.dispatch(ShabbatDataEvent.RetryLoadTimes) },
        )
    }

    LaunchedEffect(shabbatState.permission) {
        if (Debug.enabled) { Log.d("ShabbatScreen", "$shabbatState") }
        shabbatViewModel.effects.collect { effect ->
            if (Debug.enabled) { Log.d("ShabbatScreen", "$effect") }

            when (effect) {
                is AppEffect.Shabbat.OpenAppSettings -> context.openAppSettings()
                else -> Unit
            }
        }
    }
}