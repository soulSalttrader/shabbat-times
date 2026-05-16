package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.AboutItemDisplay
import il.soulSalttrader.shabbattimes.ui.SectionHeader

@Composable
fun AboutSection(
    header: String = stringResource(R.string.settings_about),
    items: List<AboutItemDisplay>,
) {
    SettingsSection {
        SectionHeader(header)

        items.forEach { item ->
            AboutItem(
                label = item.label,
                value = item.value,
                onClick = item.onClick,
            )
        }
    }
}

@Composable
private fun AboutItem(
    label: String,
    value: String,
    onClick: (() -> Unit)? = null,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        SectionHeader(text = label)

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = when (onClick != null) {
                true -> MaterialTheme.colorScheme.primary
                false -> MaterialTheme.colorScheme.onSurfaceVariant
            }
        )
    }
}