package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.common.openUrl
import il.soulSalttrader.shabbattimes.ui.SectionHeader
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIcon
import il.soulSalttrader.shabbattimes.ui.uiIcon.UiIconImage

@Composable
fun SupportSection(
    koFiUrl: String = stringResource(R.string.settings_ko_fi_url),
    header: String = stringResource(R.string.settings_support),
) {
    val context = LocalContext.current

    SettingsSection {
        SectionHeader(header)
        SettingsRow(
            onClick = { context.openUrl(koFiUrl) },
        ) {
            Column {
                Text(
                    text = stringResource(R.string.settings_support_title),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = stringResource(R.string.settings_support_subtitle),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            UiIconImage(
                icon = UiIcon.Resource(R.drawable.kofi_logo),
                contentDescription = "ko-fi symbol",
                contentColor = Color.Unspecified,
                modifier = Modifier.height(32.dp).wrapContentWidth(),
            )
        }
    }
}