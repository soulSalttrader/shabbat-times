package il.soulSalttrader.retro.shabbatApp.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.shabbatApp.viewModel.ShabbatViewModel

@Composable
fun ShabbatScreen() {
    val viewModel: ShabbatViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is ShabbatNetworkResult.Loading -> LoadingScreen()

        is ShabbatNetworkResult.Success -> ShabbatContent(
            result = (uiState as ShabbatNetworkResult.Success).data,
        )

        is ShabbatNetworkResult.Error   -> ErrorScreen(
            message = (uiState as ShabbatNetworkResult.Error).message,
            onRetry = viewModel::retry
        )
    }
}