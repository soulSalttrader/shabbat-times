package il.soulSalttrader.shabbattimes.playground.mvvm

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.shabbatApp.content.FailureScreen
import il.soulSalttrader.shabbattimes.shabbatApp.content.LoadingScreen
import il.soulSalttrader.shabbattimes.shabbatApp.content.ShabbatContent

@Composable
fun ShabbatScreenMVVM() {
    val viewModel: ShabbatViewModelMVVM = hiltViewModel()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()
    val halachicTimes by viewModel.halachicTimes.collectAsStateWithLifecycle()

    val isNotBlank = errorMessage?.isNotBlank() ?: false
    val message = errorMessage ?: "Oops! Something went wrong"

    when {
        isLoading  -> LoadingScreen()
        isNotBlank -> FailureScreen(message = message, onRetry = viewModel::retry)
        else       -> ShabbatContent(result = halachicTimes)
    }
}