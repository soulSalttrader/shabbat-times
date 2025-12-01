package il.soulSalttrader.retro.shabbatApp.playground.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.retro.shabbatApp.content.FailureScreen
import il.soulSalttrader.retro.shabbatApp.content.LoadingScreen
import il.soulSalttrader.retro.shabbatApp.content.ShabbatContent

@Composable
fun ShabbatScreenMVVM() {
    val viewModel: ShabbatViewModelMVVM = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val isLoading = uiState.isLoading
    val isNotBlank = uiState.errorMessage?.isNotBlank() ?: false
    val errorMessage = uiState.errorMessage ?: "Oops! Something went wrong"

    when {
        isLoading  -> LoadingScreen()
        isNotBlank -> FailureScreen(message = errorMessage, onRetry = viewModel::retry)

        else       -> ShabbatContent(result = uiState.results)
    }
}