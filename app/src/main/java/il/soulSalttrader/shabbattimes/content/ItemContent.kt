package il.soulSalttrader.shabbattimes.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier

@Stable
fun interface ItemContent<T> {
    @Composable
    fun Content(item: T, modifier: Modifier)
}