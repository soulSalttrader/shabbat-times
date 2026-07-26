package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.flow.Flow

@Singleton
class UserPreferencesRepositoryImpl @Inject constructor(
    private val userPreferences: UserPreferences,
): UserPreferencesRepository {
    override val shabbatPreferences: Flow<ShabbatPreferences> =
        userPreferences.shabbatPreferences()

    override suspend fun setCandleLightingOffset(offset: CandleLightingOffset) =
        userPreferences.setCandleLightingOffset(offset)

    override suspend fun setHavdalahCriterion(criterion: HavdalahCriterion) =
        userPreferences.setHavdalahCriterion(criterion)
}