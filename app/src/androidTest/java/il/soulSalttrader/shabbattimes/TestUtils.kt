package il.soulSalttrader.shabbattimes

import androidx.compose.ui.test.junit4.ComposeTestRule
import il.soulSalttrader.shabbattimes.di.FakePersistenceModule
import il.soulSalttrader.shabbattimes.model.Coordinates
import il.soulSalttrader.shabbattimes.model.SavedLocation
import kotlinx.coroutines.runBlocking
import java.time.ZoneId

fun addCard(
    rule: ComposeTestRule,
    savedLocationId: String,
    cityName: String = "Brno",
) {
    runBlocking {
        FakePersistenceModule.fakeSavedLocations.save(
            SavedLocation(
                id = savedLocationId,
                name = cityName,
                coordinates = Coordinates(0.0, 0.0),
                timeZoneId = ZoneId.systemDefault(),
            )
        )
    }
    rule.waitForIdle()
}