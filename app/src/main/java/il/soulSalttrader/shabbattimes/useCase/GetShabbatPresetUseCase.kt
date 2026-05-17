package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetShabbatPresetUseCase @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    operator fun invoke(): Flow<ShabbatPreset> = userPreferences.shabbatPreset()
}