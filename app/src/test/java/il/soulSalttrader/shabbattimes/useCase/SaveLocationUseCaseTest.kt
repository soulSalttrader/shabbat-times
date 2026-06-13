package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.model.SaveLocationResult
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.toResolvedLocation
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class SaveLocationUseCaseTest : DescribeSpec({
    lateinit var savedLocationsRepository: SavedLocationsRepository
    lateinit var useCase: SaveLocationUseCase

    beforeTest {
        savedLocationsRepository = mockk(relaxed = true)
        useCase = SaveLocationUseCase(savedLocationsRepository)
    }

    describe("PERM_EDGE_S4 - SCENARIO: Location limit reached with permission") {
        it("PERM_EDGE_S4_UC_1 - should return LimitReached when location limit is reached") {
            runTest {
                every { savedLocationsRepository.isLimitReached() } returns true

                val result = useCase(jerusalemLocation().toResolvedLocation())

                result shouldBe SaveLocationResult.LimitReached
                coVerify(exactly = 0) { savedLocationsRepository.save(any()) }
            }
        }

        it("PERM_EDGE_S4_UC_2 - should save location and return Success when limit not reached") {
            runTest {
                every { savedLocationsRepository.isLimitReached() } returns false

                val result = useCase(jerusalemLocation().toResolvedLocation())

                result shouldBe SaveLocationResult.Success
                coVerify(exactly = 1) { savedLocationsRepository.save(any()) }
            }
        }
    }
})