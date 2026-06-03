package il.soulSalttrader.shabbattimes.useCase

import il.soulSalttrader.shabbattimes.gpsLocation
import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.ui.viewModel.FakePermissionRepository
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest

class RemoveSavedLocationUseCaseTest : DescribeSpec({
    lateinit var savedLocationsRepository: SavedLocationsRepository
    lateinit var currentLocationRepository: CurrentLocationRepository
    lateinit var permissionRepository: FakePermissionRepository
    lateinit var useCase: RemoveSavedLocationUseCase

    beforeTest {
        savedLocationsRepository = mockk(relaxed = true)
        currentLocationRepository = mockk(relaxed = true)
        permissionRepository = FakePermissionRepository()
        useCase = RemoveSavedLocationUseCase(currentLocationRepository, savedLocationsRepository, permissionRepository)
    }

    describe("PERM_CARD_S2 - SCENARIO: Remove GPS card, revoke permission, re-add") {
        it("PERM_CARD_S2_UC_1 - should reset permission state to Idle when removing current location") {
            runTest {
                useCase(gpsLocation(), isCurrent = true)

                coVerify(exactly = 1) { savedLocationsRepository.remove(any()) }
                coVerify(exactly = 1) { currentLocationRepository.update(null) }
                permissionRepository.permissionState.value shouldBe LocationPermission.Idle
            }
        }

        it("PERM_CARD_S2_UC_2 - should not reset permission state when removing non-current location") {
            runTest {
                permissionRepository.updatePermissionState(LocationPermission.Granted)

                useCase(jerusalemLocation(), isCurrent = false)

                coVerify(exactly = 1) { savedLocationsRepository.remove(any()) }
                coVerify(exactly = 0) { currentLocationRepository.update(any()) }
                permissionRepository.permissionState.value shouldBe LocationPermission.Granted
            }
        }
    }
})