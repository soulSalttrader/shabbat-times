package il.soulSalttrader.retro.shabbatApp.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.core.event.ShabbatEvent
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val viewModel: ShabbatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            result = (uiState as ShabbatDataState.Success).data ?: HalachicTimesDisplay(),
            onClick = {}
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (uiState as ShabbatDataState.Failure).message,
            onRetry = { viewModel.dispatch(event = ShabbatEvent.Load) }
        )
    }
}