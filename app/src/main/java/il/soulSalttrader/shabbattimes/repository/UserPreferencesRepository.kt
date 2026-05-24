package il.soulSalttrader.shabbattimes.repository

import il.soulSalttrader.shabbattimes.settings.ShabbatPreset
import kotlinx.coroutines.flow.Flow

interface UserPreferencesRepository {
    val shabbatPreset: Flow<ShabbatPreset>
    suspend fun setShabbatPreset(preset: ShabbatPreset)
}