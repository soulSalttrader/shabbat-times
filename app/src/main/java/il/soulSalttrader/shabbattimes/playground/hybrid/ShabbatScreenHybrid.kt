package il.soulSalttrader.shabbattimes.playground.hybrid

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.content.FailureScreen
import il.soulSalttrader.shabbattimes.content.LoadingScreen
import il.soulSalttrader.shabbattimes.content.ShabbatContent
import il.soulSalttrader.shabbattimes.model.ShabbatDataState

@Composable
fun ShabbatScreenHybrid() {
    val viewModel: ShabbatViewModelHybrid = hiltViewModel()
    val uiState by viewModel.state.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatDataState.Idle    -> LoadingScreen()

        is ShabbatDataState.Loading -> LoadingScreen()

        is ShabbatDataState.Success -> ShabbatContent(
            times = (uiState as ShabbatDataState.Success).data,
            onClick = viewModel::retry,
        )

        is ShabbatDataState.Failure -> FailureScreen(
            message = (uiState as ShabbatDataState.Failure).message,
            onRetry = viewModel::retry,
        )
    }
}