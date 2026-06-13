package il.soulSalttrader.shabbattimes.ui.viewModel

import il.soulSalttrader.shabbattimes.gpsLocation
import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.ui.event.ShabbatEvent
import il.soulSalttrader.shabbattimes.useCase.RemoveSavedLocationUseCase
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class ShabbatViewModelTest : DescribeSpec({
    lateinit var removeLocationUseCase: RemoveSavedLocationUseCase
    lateinit var vm: ShabbatViewModel

    beforeTest {
        removeLocationUseCase = mockk(relaxed = true)
        vm = ShabbatViewModel(
            currentLocationRepository  = mockk(relaxed = true),
            savedLocationsRepository   = mockk(relaxed = true),
            reorderLocationsUseCase    = mockk(relaxed = true),
            getHalachicTimesUseCase    = mockk(relaxed = true),
            removeLocationUseCase      = removeLocationUseCase,
            userPreferencesRepository  = mockk(relaxed = true),
            permissionRepository       = mockk(relaxed = true),
        )
    }

    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    describe("PERM_CARD_S1 - SCENARIO: Remove GPS card then re-add") {
        it("PERM_CARD_S1_VM_1 - should call removeLocationUseCase when GPS card deleted") {
            runTest {
                val gpsLocation = gpsLocation()
                vm.dispatch(ShabbatEvent.LocationDeleted(gpsLocation, true))
                testDispatcher.scheduler.advanceUntilIdle()

                coVerify { removeLocationUseCase(gpsLocation, true) }
            }
        }

        it("PERM_CARD_S1_VM_2 - should call removeLocationUseCase with isCurrent false for non-GPS card") {
            runTest {
                val location = jerusalemLocation()
                vm.dispatch(ShabbatEvent.LocationDeleted(location, false))
                testDispatcher.scheduler.advanceUntilIdle()

                coVerify { removeLocationUseCase(location, false) }
            }
        }
    }
})