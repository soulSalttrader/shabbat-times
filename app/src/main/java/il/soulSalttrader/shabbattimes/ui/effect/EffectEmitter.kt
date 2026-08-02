package il.soulSalttrader.shabbattimes.ui.effect

import il.soulSalttrader.shabbattimes.settings.OneTimeMessage

interface EffectEmitter {
    fun emitEffect(effect: UiEffect)
    fun emitOnce(message: OneTimeMessage, effect: UiEffect)
}
