package il.soulSalttrader.shabbattimes.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import il.soulSalttrader.shabbattimes.settings.OneTimeMessage
import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.effect.EffectEmitter
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel (
    private val oneTimeMessageTracker: OneTimeMessageTracker,
) : ViewModel(), EffectEmitter {
    private val _effects = MutableSharedFlow<UiEffect>(extraBufferCapacity = 20)
    val effects: SharedFlow<UiEffect> = _effects.asSharedFlow()

    override fun emitEffect(effect: UiEffect) {
        _effects.tryEmit(effect)
    }

    override fun emitOnce(message: OneTimeMessage, effect: UiEffect) {
        viewModelScope.launch {
            if (oneTimeMessageTracker.hasShown(message)) return@launch
            emitEffect(effect)
            oneTimeMessageTracker.markShown(message)
        }
    }

    protected fun emitBatchOutcome(outcome: BatchOutcome) {
        outcome.toMessage()?.let { emitEffect(UiEffect.ShowToast(it)) }
    }
}