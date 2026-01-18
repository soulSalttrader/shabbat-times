package il.soulSalttrader.retro.shabbatApp.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.core.event.LocationEvent
import il.soulSalttrader.retro.core.event.ShabbatDataEvent
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val viewModel: ShabbatViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (state.data) {
        is ShabbatDataState.Idle  -> LoadingScreen()

        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            result = (state.data as ShabbatDataState.Success).data ?: HalachicTimesDisplay(),
            onClick = { viewModel.dispatch(event = LocationEvent.Load) }
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (state.data as ShabbatDataState.Failure).message,
            onRetry = { viewModel.dispatch(event = ShabbatDataEvent.Load) }
        )
    }
}