package il.soulSalttrader.shabbattimes.ui.viewModel

import il.soulSalttrader.shabbattimes.ui.UiText

//sealed interface BatchOutcome {
//    data object AllSucceeded : BatchOutcome
//    data class PartiallyFailed(val failedCount: Int, val totalCount: Int, val sampleMessage: UiText) : BatchOutcome
//    data class AllFailed(val message: UiText) : BatchOutcome
//}


sealed interface BatchOutcome {
    data object AllSucceeded : BatchOutcome
    data class PartiallyFailed(
        val failedCount: Int,
        val totalCount: Int,
        val sampleMessage: UiText,
    ) : BatchOutcome
    data class AllFailed(val message: UiText) : BatchOutcome
}