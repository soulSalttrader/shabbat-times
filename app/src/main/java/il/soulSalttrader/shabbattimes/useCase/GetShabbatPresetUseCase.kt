package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject

class GetShabbatPresetUseCase @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    suspend operator fun invoke(): ShabbatPreset = userPreferences.shabbatPreset()
}