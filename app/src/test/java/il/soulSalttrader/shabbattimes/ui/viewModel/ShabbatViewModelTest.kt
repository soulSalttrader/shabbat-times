package il.soulSalttrader.shabbattimes.ui.viewModel

import app.cash.turbine.test
import il.soulSalttrader.shabbattimes.R
import il.soulSalttrader.shabbattimes.gpsLocation
import il.soulSalttrader.shabbattimes.jerusalemLocation
import il.soulSalttrader.shabbattimes.model.ShabbatCalendar
import il.soulSalttrader.shabbattimes.network.FakeNetworkObserver
import il.soulSalttrader.shabbattimes.repository.CurrentLocationRepository
import il.soulSalttrader.shabbattimes.repository.SavedLocationsRepository
import il.soulSalttrader.shabbattimes.repository.UserPreferencesRepository
import il.soulSalttrader.shabbattimes.settings.ShabbatPreferences
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarDepressionTimeCalculator
import il.soulSalttrader.shabbattimes.ui.UiText
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.ShabbatEvent
import il.soulSalttrader.shabbattimes.useCase.GetHalachicTimesUseCase
import il.soulSalttrader.shabbattimes.useCase.RemoveSavedLocationUseCase
import io.kotest.common.ExperimentalKotest
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class, ExperimentalKotest::class)
class ShabbatViewModelTest : DescribeSpec({
    lateinit var removeLocationUseCase: RemoveSavedLocationUseCase
    lateinit var fakeNetwork: FakeNetworkObserver
    lateinit var vm: ShabbatViewModel
    lateinit var getHalachicTimesUseCase: GetHalachicTimesUseCase
    lateinit var fakeSolarTimesRepository: FakeSolarTimesRepository
    lateinit var shabbatCalendar: ShabbatCalendar
    lateinit var havdalahCalculator: SolarDepressionTimeCalculator
    lateinit var shabbatPreferences: ShabbatPreferences

    beforeTest {
        removeLocationUseCase = mockk(relaxed = true)
        fakeNetwork = FakeNetworkObserver(initialConnected = false)
        fakeSolarTimesRepository = FakeSolarTimesRepository()
        shabbatCalendar = mockk(relaxed = true)
        havdalahCalculator = mockk(relaxed = true)
        shabbatPreferences = mockk(relaxed = true)
        getHalachicTimesUseCase = GetHalachicTimesUseCase(fakeSolarTimesRepository, shabbatCalendar, havdalahCalculator)

        val currentLocationRepository = mockk<CurrentLocationRepository>(relaxed = true)
        val savedLocationsRepository = mockk<SavedLocationsRepository>(relaxed = true)
        val userPreferencesRepository = mockk<UserPreferencesRepository>(relaxed = true)

        every { currentLocationRepository.location } returns MutableStateFlow(null)
        every { savedLocationsRepository.locations } returns MutableStateFlow(listOf(jerusalemLocation()))
        every { userPreferencesRepository.shabbatPreferences } returns MutableStateFlow(shabbatPreferences)

        vm = ShabbatViewModel(
            currentLocationRepository = currentLocationRepository,
            savedLocationsRepository = savedLocationsRepository,
            reorderLocationsUseCase = mockk(relaxed = true),
            getHalachicTimesUseCase = getHalachicTimesUseCase,
            removeLocationUseCase = removeLocationUseCase,
            observeGpsLocationUseCase = mockk(relaxed = true),
            userPreferencesRepository = userPreferencesRepository,
            permissionRepository = mockk(relaxed = true),
            networkConnectivityObserver = fakeNetwork,
            oneTimeMessageTracker = mockk(relaxed = true),
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
                advanceUntilIdle()

                coVerify { removeLocationUseCase(gpsLocation, true) }
            }
        }

        it("PERM_CARD_S1_VM_2 - should call removeLocationUseCase with isCurrent false for non-GPS card") {
            runTest {
                val location = jerusalemLocation()
                vm.dispatch(ShabbatEvent.LocationDeleted(location, false))
                advanceUntilIdle()

                coVerify { removeLocationUseCase(location, false) }
            }
        }
    }

    describe("NETWORK_CONNECT_1 - SCENARIO: Network connectivity effects") {
        it("NETWORK_CONNECT_1_S1 - should show restored internet toast when connection comes back") {
            runTest {
                vm.effects.test {
                    fakeNetwork.setConnected(true)
                    advanceUntilIdle()

                    awaitItem() shouldBe UiEffect.ShowToast(UiText.Resource(R.string.restored_internet))

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("NETWORK_CONNECT_1_S2 - should show no internet toast when connection is lost") {
            runTest {
                vm.effects.test {
                    fakeNetwork.setConnected(true)
                    advanceUntilIdle()
                    awaitItem() shouldBe UiEffect.ShowToast(UiText.Resource(R.string.restored_internet))

                    fakeNetwork.setConnected(false)
                    advanceUntilIdle()
                    awaitItem() shouldBe UiEffect.ShowToast(UiText.Resource(R.string.error_no_internet))

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("NETWORK_CONNECT_2 - SCENARIO: Reload trigger behavior") {
        it("NETWORK_CONNECT_2_S1 - should reload halachic times when connection is restored") {
            runTest(testDispatcher) {
                backgroundScope.launch { vm.halachicTimesFlow.collect() }
                advanceUntilIdle()
                val callsAfterInitialLoad = fakeSolarTimesRepository.callCount
                fakeNetwork.setConnected(true)
                advanceUntilIdle()
                fakeSolarTimesRepository.callCount shouldBe callsAfterInitialLoad * 2 // friday, saturday
            }
        }

        it("NETWORK_CONNECT_2_S2 - should NOT reload when connection is lost") {
            runTest(testDispatcher) {
                backgroundScope.launch { vm.halachicTimesFlow.collect() }
                advanceUntilIdle()
                val callsAfterInitialLoad = fakeSolarTimesRepository.callCount
                fakeNetwork.setConnected(true)
                advanceUntilIdle()
                fakeNetwork.setConnected(false)
                advanceUntilIdle()
                fakeSolarTimesRepository.callCount shouldBe callsAfterInitialLoad * 2 // friday, saturday
            }
        }
    }
})
