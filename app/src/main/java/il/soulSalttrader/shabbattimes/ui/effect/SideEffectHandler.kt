package il.soulSalttrader.shabbattimes.ui.effect

import il.soulSalttrader.shabbattimes.ui.event.UiEvent
import kotlinx.coroutines.flow.MutableSharedFlow

interface SideEffectHandler<E : UiEvent> {
    fun handle(event: E, effects: MutableSharedFlow<UiEffect>)
}