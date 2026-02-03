package il.soulSalttrader.shabbattimes.content

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em

@Composable
fun UiIconLabel(
    icon: UiIcon?,
    iconId: String = "currentLocationIcon",
    text: String = "Your current location",
    contentDescription: String = "Current location icon",
    iconSize: TextUnit = 1.1.em,
) {
    icon?.let {
        val inlineContent = mapOf(
            iconId to InlineTextContent(
                placeholder = Placeholder(
                    width = iconSize,
                    height = iconSize,
                    placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
                ),
                children = {
                    UiIconImage(
                        icon = icon,
                        contentDescription = contentDescription,
                    )
                }
            )
        )

        val annotatedString = buildAnnotatedString {
            appendInlineContent(iconId, text)
            append(" $text")
        }

        Text(
            text = annotatedString,
            inlineContent = inlineContent,
            style = MaterialTheme.typography.bodyMedium,
        )
    } ?:

    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
    )
}