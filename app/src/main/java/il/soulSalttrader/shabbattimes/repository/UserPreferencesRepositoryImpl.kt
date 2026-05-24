package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences,
): UserPreferencesRepository {
    override val shabbatPreset: Flow<ShabbatPreset> = userPreferences.shabbatPreset()

    override suspend fun setShabbatPreset(preset: ShabbatPreset) =
        userPreferences.setShabbatPreset(preset)
}