package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val shabbatPreferences: Flow<ShabbatPreferences>
    suspend fun setCandleLightingOffset(offset: CandleLightingOffset)
    suspend fun setHavdalahCriterion(criterion: HavdalahCriterion)
}