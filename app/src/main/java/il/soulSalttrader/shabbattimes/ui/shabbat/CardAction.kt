package il.soulSalttrader.shabbattimes.ui.shabbat

interface CardAction {
    data object OpenGpsSearch : CardAction
    data object AcceptRationale : CardAction
    data object ShowDeniedDialog : CardAction
    data object ShowEducation : CardAction
}