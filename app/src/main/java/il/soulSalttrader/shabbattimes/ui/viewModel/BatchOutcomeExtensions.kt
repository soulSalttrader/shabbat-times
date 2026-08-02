package il.soulSalttrader.shabbattimes.ui.viewModel

import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.ui.UiText

fun BatchOutcome.toMessage(): UiText? = when (this) {
    is BatchOutcome.AllSucceeded -> null
    is BatchOutcome.AllFailed -> message
    is BatchOutcome.PartiallyFailed -> UiText.Resource(
        id = R.string.batch_partial_failure,
        args = listOf(failedCount, totalCount),
    )
}
