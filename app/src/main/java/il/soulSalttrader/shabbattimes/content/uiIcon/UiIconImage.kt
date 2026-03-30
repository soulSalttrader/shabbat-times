package il.soulSalttrader.shabbattimes.content.uiIcon

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun UiIconImage(
    modifier: Modifier = Modifier,
    icon: UiIcon,
    contentDescription: String?,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    when (icon) {
        is UiIcon.Vector -> {
            Icon(
                modifier = modifier,
                imageVector = icon.image,
                contentDescription = contentDescription,
                tint = contentColor,
            )
        }

        is UiIcon.Resource -> {
            Icon(
                modifier = modifier,
                painter = painterResource(id = icon.resId),
                contentDescription = contentDescription,
                tint = contentColor,
            )
        }
    }
}