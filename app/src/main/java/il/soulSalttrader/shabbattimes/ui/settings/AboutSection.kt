package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
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
    SettingsRow(onClick = onClick) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
        )

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