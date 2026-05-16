package il.soulSalttrader.shabbattimes.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import il.soulSalttrader.shabbattimes.BuildConfig
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.settings.AboutItemDisplay
import il.soulSalttrader.shabbattimes.ui.event.SettingsEvent
import il.soulSalttrader.shabbattimes.ui.viewModel.SettingsViewModel

@Composable
fun SettingsScreen() {
    val settingsViewModel: SettingsViewModel = hiltViewModel()
    val settingsUiState by settingsViewModel.state.collectAsStateWithLifecycle()

    val items = listOf(
        AboutItemDisplay(
            label = stringResource(R.string.settings_version),
            value = BuildConfig.VERSION_NAME,
        ),
        AboutItemDisplay(
            label = stringResource(R.string.settings_developer),
            value = stringResource(R.string.settings_developer_name),
        ),
        AboutItemDisplay(
            label = stringResource(R.string.settings_contact),
            value = stringResource(R.string.settings_contact_email),
            onClick = { TODO("Add email intent") },
        ),
    )

    SettingsContent(
        items = items,
        state = settingsUiState,
        onPresetSelected = { preset ->  settingsViewModel.dispatch(SettingsEvent.PresetSelected(preset)) },
    )
}