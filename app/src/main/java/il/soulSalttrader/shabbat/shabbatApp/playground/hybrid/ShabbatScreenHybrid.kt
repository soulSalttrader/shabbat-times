package il.soulSalttrader.retro.shabbatApp.playground.hybrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.shabbatApp.content.FailureScreen
import il.soulSalttrader.retro.shabbatApp.content.LoadingScreen
import il.soulSalttrader.retro.shabbatApp.content.ShabbatContent
import il.soulSalttrader.retro.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.retro.shabbatApp.model.ShabbatDataState

@Composable
fun ShabbatScreenHybrid() {
    val viewModel: ShabbatViewModelHybrid = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            result = (uiState as ShabbatDataState.Success).data ?: HalachicTimesDisplay(),
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (uiState as ShabbatDataState.Failure).message,
            onRetry = viewModel::retry
        )
    }
}