package il.soulSalttrader.shabbattimes.content

import androidx.compose.runtime.Composable
import il.soulSalttrader.shabbattimes.R

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