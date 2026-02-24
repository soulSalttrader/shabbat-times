package il.soulSalttrader.shabbattimes.content

import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.R
@Composable
private fun TrailingSearchIconButton(
    onClick: () -> Unit,
    resId: Int = R.drawable.cancel_24px,
    contentDescription: String? = "search_trailing",
) {
    IconButton(onClick = onClick) {
        UiIconImage(
            icon = UiIcon.Resource(resId = resId),
            contentDescription = contentDescription,
        )
    }
}

@Composable
private fun LeadingSearchIcon(
    resId: Int = R.drawable.search_24px,
    contentDescription: String? = "search_leading",
) {
    UiIconImage(
        icon = UiIcon.Resource(resId = resId),
        contentDescription = contentDescription,
    )
}