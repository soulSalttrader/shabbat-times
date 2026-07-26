package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.settings.CandleLightingOffset
import il.soulSalttrader.shabbattimes.settings.HavdalahCriterion
import il.soulSalttrader.shabbattimes.settings.UserPreferences
import jakarta.inject.Inject

class SaveShabbatPreferenceUseCase @Inject constructor(
    private val userPreferences: UserPreferences,
) {
    suspend operator fun invoke(criterion: HavdalahCriterion) {
        userPreferences.setHavdalahCriterion(criterion)
    }

    suspend operator fun invoke(offset: CandleLightingOffset) {
        userPreferences.setCandleLightingOffset(offset)
    }
}