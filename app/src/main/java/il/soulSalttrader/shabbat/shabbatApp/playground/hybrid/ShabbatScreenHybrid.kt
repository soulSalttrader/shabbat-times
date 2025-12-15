package il.soulSalttrader.retro.shabbatApp.playground.hybrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.shabbatApp.content.FailureScreen
import il.soulSalttrader.retro.shabbatApp.content.LoadingScreen
import il.soulSalttrader.retro.shabbatApp.content.ShabbatContent
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatUiState

@Composable
fun ShabbatScreenHybrid() {
    val viewModel: ShabbatViewModelHybrid = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatUiState.Loading -> LoadingScreen()

        is ShabbatUiState.Success -> ShabbatContent(
            result = (uiState as ShabbatUiState.Success).data ?: HalachicTimesDisplay(),
        )

        is ShabbatUiState.Failure -> FailureScreen(
            message = (uiState as ShabbatUiState.Failure).message,
            onRetry = viewModel::retry
        )
    }
}