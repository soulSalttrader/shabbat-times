package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ShabbatPreferencesUseCase @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    operator fun invoke(): Flow<ShabbatPreferences> = userPreferences.shabbatPreferences()
}