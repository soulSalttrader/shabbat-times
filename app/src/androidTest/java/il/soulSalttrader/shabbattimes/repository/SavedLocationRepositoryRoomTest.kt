package il.soulSalttrader.shabbattimes.repository

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import il.soulSalttrader.shabbattimes.brnoLocation
import il.soulSalttrader.shabbattimes.data.AppDatabase
import il.soulSalttrader.shabbattimes.data.SavedLocationDao
import il.soulSalttrader.shabbattimes.gpsLocation
import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.model.SavedLocation
import il.soulSalttrader.shabbattimes.telAvivLocation
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest

@RunWith(AndroidJUnit4::class)
class SavedLocationsRepositoryRoomTest {

    private val testDispatcher = StandardTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private lateinit var db: AppDatabase
    private lateinit var repo: SavedLocationsRepositoryRoom
    private lateinit var dao: SavedLocationDao

    @Before
    fun setup() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java,
        )
            .allowMainThreadQueries()
            .build()

        dao = db.savedLocationDao()

        repo = SavedLocationsRepositoryRoom(
            dao = db.savedLocationDao(),
            scope = testScope.backgroundScope,
        )
    }

    @After
    fun tearDown() {
        db.close()
        testScope.cancel()
    }

    @Test
    fun `REPO_CARD_REORDER_S1 - should persist new sort order after reorder`() = testScope.runTest {
        repo.save(jerusalemLocation())
        repo.save(telAvivLocation())
        repo.save(brnoLocation())
        repo.locations.first { it.size == 3 }

        repo.reorder(
            listOf(
                jerusalemLocation(),
                brnoLocation(),
                telAvivLocation(),
            ),
        )

        val result = dao.observeAll().first()
        assert(result[0].id == "id_1") { "expected id_1 first" }
        assert(result[1].id == "id_3") { "expected id_3 second" }
        assert(result[2].id == "id_2") { "expected id_2 third" }
    }

    @Test
    fun `REPO_CARD_REORDER_S2 - should persist sort order when card moved down`() = testScope.runTest {
        repo.save(jerusalemLocation())
        repo.save(telAvivLocation())
        repo.save(brnoLocation())

        repo.locations.first { it.size == 3 }

        repo.reorder(
            listOf(
                brnoLocation(),
                telAvivLocation(),
                jerusalemLocation(),
            ),
        )

        val result = dao.observeAll().first()
        assert(result[0].id == "id_3") { "expected id_3 first" }
        assert(result[1].id == "id_2") { "expected id_2 second" }
        assert(result[2].id == "id_1") { "expected id_1 third" }
    }

    @Test
    fun `REPO_CARD_REORDER_S3 - should persist sort order when GPS card reordered`() = testScope.runTest {
        repo.save(jerusalemLocation())
        repo.save(gpsLocation())
        repo.save(telAvivLocation())
        repo.save(brnoLocation())

        repo.locations.first { it.size == 4 }

        repo.reorder(
            listOf(
                gpsLocation(),
                jerusalemLocation(),
                telAvivLocation(),
                brnoLocation(),
            ),
        )

        val result = dao.observeAll().first()
        assert(result[0].id == SavedLocation.GPS_ID) { "expected ${SavedLocation.GPS_ID} first" }
        assert(result[1].id == "id_1") { "expected id_1 second" }
        assert(result[2].id == "id_2") { "expected id_2 third" }
        assert(result[3].id == "id_3") { "expected id_3 fourth" }
    }
}
