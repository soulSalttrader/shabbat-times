package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

abstract class BaseViewModel : ViewModel() {
    private val _effects = MutableSharedFlow<UiEffect>(extraBufferCapacity = 20)
    val effects: SharedFlow<UiEffect> = _effects.asSharedFlow()

    protected fun emitEffect(effect: UiEffect) {
        _effects.tryEmit(effect)
    }

    protected fun emitBatchOutcome(outcome: BatchOutcome) {
        val message: UiText = when (outcome) {
            is BatchOutcome.AllSucceeded -> return
            is BatchOutcome.AllFailed -> outcome.message
            is BatchOutcome.PartiallyFailed -> UiText.Resource(
                id = R.string.batch_partial_failure,
                args = listOf(outcome.failedCount, outcome.totalCount),
            )
        }
        emitEffect(UiEffect.ShowToast(message))
    }
}