package il.soulSalttrader.shabbattimes.playground.hybrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.shabbatApp.content.FailureScreen
import il.soulSalttrader.shabbattimes.shabbatApp.content.LoadingScreen
import il.soulSalttrader.shabbattimes.shabbatApp.content.ShabbatContent
import il.soulSalttrader.shabbattimes.shabbatApp.model.HalachicTimesDisplay
import il.soulSalttrader.shabbattimes.shabbatApp.model.ShabbatDataState

@Composable
fun ShabbatScreenHybrid() {
    val viewModel: ShabbatViewModelHybrid = hiltViewModel()

    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatDataState.Idle    -> LoadingScreen()

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