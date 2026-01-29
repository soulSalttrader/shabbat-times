package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun IconTextButton(
    modifier: Modifier = Modifier,
    icon: UiIcon? = null,
    text: String? = null,
    style: TextStyle = MaterialTheme.typography.labelMedium,
    contentDescription: String? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary
    ),
    shape: Shape = CircleShape,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.size(56.dp),
        colors = colors,
        shape = shape,
        contentPadding = PaddingValues.Zero,
        enabled = enabled,
        onClick = onClick,
    ) {
        icon?.let {
            UiIconImage(
                icon = icon,
                contentDescription = contentDescription,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            )
        }

        text?.let {
            Text(
                text = text,
                style = style,
                textAlign = TextAlign.Center,
            )
        }
    }
}