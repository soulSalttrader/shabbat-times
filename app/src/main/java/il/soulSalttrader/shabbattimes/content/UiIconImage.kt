package il.soulSalttrader.shabbattimes.content

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

@Composable
fun UiIconImage(
    icon: UiIcon,
    contentDescription: String?,
    contentColor: Color = MaterialTheme.colorScheme.onSurfaceVariant,
) {
    when (icon) {
        is UiIcon.Vector -> {
            Icon(
                imageVector = icon.image,
                contentDescription = contentDescription,
                tint = contentColor,
            )
        }

        is UiIcon.Resource -> {
            Icon(
                painter = painterResource(id = icon.resId),
                contentDescription = contentDescription,
                tint = contentColor,
            )
        }
    }
}