package il.soulSalttrader.shabbattimes.ui.uiIcon

import androidx.compose.foundation.text.InlineTextContent
import androidx.compose.foundation.text.appendInlineContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.PlaceholderVerticalAlign
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.em

@Composable
fun UiIconLabel(
    text: String,
    modifier: Modifier = Modifier,
    leadingIcon: UiIcon? = null,
    trailingIcon: UiIcon? = null,
    leadingIconId: String = "leadingIcon",
    trailingIconId: String = "trailingIcon",
    style: TextStyle = MaterialTheme.typography.bodyMedium,
    leadingContentDescription: String = "leadingIconLabel",
    trailingContentDescription: String = "trailingIconLabel",
    contentColor: Color = MaterialTheme.colorScheme.onPrimaryContainer,
    iconSize: TextUnit = 1.329.em,
) {
    val inlineContent = buildMap {
        leadingIcon?.let {
            put(leadingIconId, inlineIconContent(iconSize, it, contentColor, leadingContentDescription))
        }
        trailingIcon?.let {
            put(trailingIconId, inlineIconContent(iconSize, it, contentColor, trailingContentDescription))
        }
    }

    val annotatedString = buildAnnotatedString {
        if (leadingIcon != null) {
            appendInlineContent(leadingIconId)
            append(" ")
        }
        append(text)
        if (trailingIcon != null) {
            append(" ")
            appendInlineContent(trailingIconId)
        }
    }

    Text(
        modifier = modifier,
        text = annotatedString,
        inlineContent = inlineContent,
        style = style,
    )
}

private fun inlineIconContent(
    iconSize: TextUnit,
    icon: UiIcon,
    contentColor: Color,
    contentDescription: String,
) = InlineTextContent(
    placeholder = Placeholder(
        width = iconSize,
        height = iconSize,
        placeholderVerticalAlign = PlaceholderVerticalAlign.Center,
    ),
    children = {
        UiIconImage(
            icon = icon,
            contentColor = contentColor,
            contentDescription = contentDescription,
        )
    },
)
