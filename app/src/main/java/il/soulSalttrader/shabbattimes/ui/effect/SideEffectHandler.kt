package il.soulSalttrader.shabbattimes.ui.effect

import il.soulSalttrader.shabbattimes.ui.event.UiEvent

interface SideEffectHandler<E : UiEvent> {
    fun handle(event: E, emitter: EffectEmitter)
}