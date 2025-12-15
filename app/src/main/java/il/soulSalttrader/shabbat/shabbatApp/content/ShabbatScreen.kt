package il.soulSalttrader.retro.shabbatApp.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatEvent
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val viewModel: ShabbatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatUiState.Loading -> LoadingScreen()

        is ShabbatUiState.Success -> ShabbatContent(
            result = (uiState as ShabbatUiState.Success).data,
        )

        is ShabbatUiState.Failure -> FailureScreen(
            message = (uiState as ShabbatUiState.Failure).message,
            onRetry = { viewModel.dispatch(event = ShabbatEvent.Load) }
        )
    }
}