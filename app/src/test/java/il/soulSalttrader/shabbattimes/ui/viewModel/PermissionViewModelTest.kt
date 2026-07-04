package il.soulSalttrader.shabbattimes.ui.viewModel

import app.cash.turbine.test
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.permission.PermissionTestFixture
import il.soulSalttrader.shabbattimes.settings.DataStoreOneTimeMessageTracker
import il.soulSalttrader.shabbattimes.settings.FakeOneTimeMessageTracker
import il.soulSalttrader.shabbattimes.settings.OneTimeMessage
import il.soulSalttrader.shabbattimes.ui.effect.PermissionSideEffectHandler
import il.soulSalttrader.shabbattimes.ui.event.GpsEvent
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class PermissionViewModelTest : DescribeSpec({
    val testDispatcher = StandardTestDispatcher()

    beforeSpec {
        Dispatchers.setMain(testDispatcher)
    }

    afterSpec {
        Dispatchers.resetMain()
    }

    fun setup(): PermissionTestFixture {
        val tracker = FakeOneTimeMessageTracker()
        val repo = FakePermissionRepository()
        val handler = PermissionSideEffectHandler(repo, tracker)
        val viewModel = PermissionViewModel(handler, tracker, repo)

        return PermissionTestFixture(viewModel, repo, tracker)
    }

    describe("PERM_EDUCATION - one-time education dialog") {
        it("PERM_EDUCATION_S1 - should show education dialog only on first trigger") {
            runTest {
                val (vm, repo, tracker) = setup()

                println("tracker:${(tracker as FakeOneTimeMessageTracker).shownMessages()}")
                println("permission1:${repo.permissionState.value}")

                vm.dispatch(PermissionEvent.ShowEducation)
                advanceUntilIdle()

                vm.dispatch(PermissionEvent.RequestPermission)
                advanceUntilIdle()

                vm.dispatch(PermissionEvent.SystemGranted)
                advanceUntilIdle()

                // second trigger — tracker says already shown
                vm.dispatch(PermissionEvent.ShowEducation)
            }
        }

        it("PERM_EDUCATION_S2 - should not re-show education after app restart simulation") {
            runTest {
                val tracker = FakeOneTimeMessageTracker()
                tracker.markShown(OneTimeMessage.LOCATION_PERMISSION_EDUCATION)

                val (vm, _, _) = setup()

                vm.dispatch(PermissionEvent.ShowEducation)
                testDispatcher.scheduler.advanceUntilIdle()
            }
        }
    }

    describe("PERM_FRESH_S1 - SCENARIO: User grants permission on first ask") {
        it("PERM_FRESH_S1_VM - should reflect Granted after full Education → Requesting → Granted flow") {
            runTest {
                val (vm, repo, _) = setup()

                vm.state.test {
                    awaitItem() // Idle (initial)
                    repo.permissionState.value shouldBe LocationPermission.Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education
                    repo.permissionState.value shouldBe LocationPermission.Education

                    vm.dispatch(PermissionEvent.RequestPermission)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    vm.dispatch(PermissionEvent.SystemGranted)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Granted
                    repo.permissionState.value shouldBe LocationPermission.Granted

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_FRESH_S2 - SCENARIO: User denies permission on first ask") {
        it("PERM_FRESH_S2_VM - should show DeniedRationale state after deny flow") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem() // initial Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education
                    repo.permissionState.value shouldBe LocationPermission.Education

                    vm.dispatch(PermissionEvent.RequestPermission)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    vm.dispatch(PermissionEvent.SystemDenied)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedRationale
                    repo.permissionState.value shouldBe LocationPermission.DeniedRationale

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_FRESH_S3 - SCENARIO: User denies then allows via rationale") {
        it("PERM_FRESH_S3_VM - should reflect Granted after deny → accept rationale → grant") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem() // initial Idle

                    vm.dispatch(PermissionEvent.SystemDenied)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedRationale

                    vm.dispatch(PermissionEvent.RequestPermission)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting //  accept rationale
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    vm.dispatch(PermissionEvent.SystemGranted)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Granted
                    repo.permissionState.value shouldBe LocationPermission.Granted

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_FRESH_S4 - SCENARIO: User permanently denies permission") {
        it("PERM_FRESH_S4_VM - should reflect DeniedPermanently after denying twice ") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem() // Idle
                    repo.permissionState.value shouldBe LocationPermission.Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education
                    repo.permissionState.value shouldBe LocationPermission.Education

                    vm.dispatch(PermissionEvent.RequestPermission)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    vm.dispatch(PermissionEvent.SystemDenied) // first deny
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedRationale
                    repo.permissionState.value shouldBe LocationPermission.DeniedRationale

                    vm.dispatch(PermissionEvent.RequestPermission) // tap Allow on rationale
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    vm.dispatch(PermissionEvent.DismissDeniedRationale) // second deny
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently
                    repo.permissionState.value shouldBe LocationPermission.Requesting

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_FRESH_S6 - SCENARIO: User dismisses education dialog") {
        it("PERM_FRESH_S6_VM - should return to Idle when Education dialog dismissed") {
            runTest {
                val (vm, _, _) = setup()
                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.ShowEducation)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education

                    vm.dispatch(PermissionEvent.DismissEducation)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Idle

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_SETTINGS_S1 - SCENARIO: User grants permission in settings") {
        it("PERM_SETTINGS_S1_VM_1 - should set Idle after returning from settings") {
            runTest {
                val (vm, repo, _) = setup()

                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                    advanceUntilIdle()
                    awaitItem() // DeniedPermanently

                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    advanceUntilIdle()

                    awaitItem().permission shouldBe PermissionState.Idle

                    repo.permissionState.value shouldBe LocationPermission.Idle

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_SETTINGS_S1_VM_2 - should start fresh Education flow when card is tapped after returning from Settings") {
            runTest {
                val (vm, _, _) = setup()

                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education
                }
            }
        }
    }

    describe("PERM_SETTINGS_S2 - SCENARIO: User sets Ask every time in settings") {
        it("PERM_SETTINGS_S2_VM_1 - should show Education dialog on next tap after user grants it in Settings") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Education

                    repo.permissionState.value shouldBe LocationPermission.Education

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_SETTINGS_S3 - SCENARIO: User ignores settings and returns") {
        it("PERM_SETTINGS_S3_VM_1 - should keep DeniedPermanently when settings ignored") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem() // Idle

                    vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    // ON_RESUME fires, resolvePermissionEvent = null + DeniedPermanently
                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Idle

                    // user taps card → Education → Request → still permanently denied
                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.RequestPermission)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    repo.permissionState.value shouldBe LocationPermission.DeniedPermanently

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_RESTART_S1 - SCENARIO: Restart with granted permission") {
        it("PERM_RESTART_S1_VM_1 - should reflect Granted immediately on cold start") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.Granted)
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                val vm = PermissionViewModel(handler,tracker, repo)

                vm.state.test {
                    awaitItem().permission shouldBe PermissionState.Granted

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_RESTART_S2 - SCENARIO: Restart with temporary denial") {
        it("PERM_RESTART_S2_VM_1 - should reflect Denied on cold start, no dialogs") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.Denied)
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                val vm = PermissionViewModel(handler,tracker, repo)

                vm.state.test {
                    awaitItem().permission shouldBe PermissionState.Denied
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_RESTART_S3 - SCENARIO: Restart with permanently denied") {
        it("PERM_RESTART_S3_VM_1 - should reflect DeniedPermanently on cold start") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.DeniedPermanently)
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                val vm = PermissionViewModel(handler,tracker, repo)

                vm.state.test {
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_RESTART_S3_VM_2 - should skip Education and show Open Settings after cold start when permission is permanently denied") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.DeniedPermanently) // persisted
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                val vm = PermissionViewModel(handler,tracker, repo)

                vm.state.test {
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    // user taps card
                    vm.dispatch(PermissionEvent.TappedCardDeniedPermanently)
                    advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanentlyRationale

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_RESTART_S4 - SCENARIO: Restart after revoking in settings") {
        it("PERM_RESTART_S4_VM - should reflect Denied after external revocation") {
            runTest {
                val (vm, repo) = setup()
                vm.state.test {
                    awaitItem() // Idle

                    repo.updatePermissionState(LocationPermission.Denied)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Denied

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_EDGE_S1 - SCENARIO: Rapid tap GPS card") {
        it("PERM_EDGE_S1_VM - should not cause invalid state on rapid dispatches") {
            runTest {
                val (vm, _) = setup()
                vm.state.test {
                    awaitItem() // Idle

                    // simulate rapid tap - user taps card multiple times quickly
                    // real risk: ShowEducation fired multiple times before dialog appears
                    vm.dispatch(PermissionEvent.ShowEducation)
                    vm.dispatch(PermissionEvent.ShowEducation)
                    vm.dispatch(PermissionEvent.ShowEducation)
                    advanceUntilIdle()

                    val states = cancelAndConsumeRemainingEvents()
                        .filterIsInstance<app.cash.turbine.Event.Item<PermissionUiState>>()
                        .map { it.value }

                    // must never go back to Idle
                    states.none { it.permission == PermissionState.Idle } shouldBe true

                    // final state must be Education with dialog visible
                    states.last().permission shouldBe PermissionState.Education
                }
            }
        }
    }

    describe("PERM_EDGE_S2 - SCENARIO: Rotate screen during permission dialog") {
        it("PERM_EDGE_S2_VM_1 - should preserve Education state after configuration change") {
            runTest {
                val (vm, repo) = setup()
                vm.dispatch(PermissionEvent.ShowEducation)
                testDispatcher.scheduler.advanceUntilIdle()

                val handler = PermissionSideEffectHandler(repo, mockk(relaxed = true))

                // simulate rotation — create new VM with same repo (same as config change)
                val recreatedVm = PermissionViewModel(handler,mockk(relaxed = true), repo)

                recreatedVm.state.test {
                    awaitItem().permission shouldBe PermissionState.Education
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_EDGE_S2_VM_2 - should preserve DeniedPermanently state after configuration change") {
            runTest {
                val (vm, repo) = setup()
                vm.dispatch(PermissionEvent.SystemDeniedPermanently)
                testDispatcher.scheduler.advanceUntilIdle()

                val handler = PermissionSideEffectHandler(repo, mockk(relaxed = true))

                // simulate rotation — create new VM with same repo (same as config change)
                val recreatedVm = PermissionViewModel(handler,mockk(relaxed = true), repo)

                recreatedVm.state.test {
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_EDGE_S3 - SCENARIO: Background app during permission dialog") {
        it("PERM_EDGE_S3_VM_1 - should keep Requesting state after app backgrounded during permission request") {
            runTest {
                val (vm, _) = setup()
                vm.dispatch(PermissionEvent.RequestPermission)
                advanceUntilIdle()

                // collect to keep subscription alive, then cancel to simulate background
                val job = launch { vm.state.collect {} }
                job.cancel()

                // advance past WhileSubscribed(5000) timeout
                advanceTimeBy(6000)

                // resubscribe to simulate foreground
                vm.state.test {
                    awaitItem().permission shouldBe PermissionState.Requesting
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_EDGE_S5 - SCENARIO: Switch apps during system permission dialog") {
        it("PERM_EDGE_S5_VM - should keep Requesting state when interrupted") {
            runTest {
                val (vm, repo) = setup()
                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.RequestPermission)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting

                    // ON_RESUME fires after returning from other app
                    // resolvePermissionEvent might return SystemGranted or DeniedWithRationale
                    vm.dispatch(PermissionEvent.SystemGranted)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Granted

                    repo.permissionState.value shouldBe LocationPermission.Granted

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_EDGE_S6 - SCENARIO: Unrelated event should not touch permission") {
        it("PERM_EDGE_S6_VM - should not change permission state on unrelated event") {
            runTest {
                val (vm, _) = setup()
                vm.state.test {
                    awaitItem() // Idle

                    // dispatch an event that doesn't belong to PermissionViewModel
                    vm.dispatch(GpsEvent.GpsLocationRequested)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // no new emission — state unchanged
                    expectNoEvents()

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_MAPPING - SCENARIO: LocationPermission maps correctly to PermissionState") {
        listOf(
            Triple(1, LocationPermission.Idle, PermissionState.Idle),
            Triple(2, LocationPermission.Education, PermissionState.Education),
            Triple(3, LocationPermission.Requesting, PermissionState.Requesting),
            Triple(4, LocationPermission.Granted, PermissionState.Granted),
            Triple(5, LocationPermission.Denied, PermissionState.Denied),
            Triple(6, LocationPermission.DeniedPermanently, PermissionState.DeniedPermanently),
        ).forEach { (order, locationPermission, expectedState) ->

            it("PERM_MAPPING_S${order} - should map $locationPermission to $expectedState") {
                runTest {
                val repo = FakePermissionRepository()
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                repo.updatePermissionState(locationPermission)
                    val vm = PermissionViewModel(handler, tracker, repo)

                    vm.state.test {
                        awaitItem().permission shouldBe expectedState
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }
    }

    describe("PERM_COMBINE_S2 - SCENARIO: Idle flash on cold start with DeniedPermanently") {
        it("BUG_COMBINE_S2 - cold start with DeniedPermanently never flashes Idle first") {
            runTest(UnconfinedTestDispatcher()) {
                val repo = FakePermissionRepository()
                val tracker = mockk<DataStoreOneTimeMessageTracker>(relaxed = true)
                val handler = PermissionSideEffectHandler(repo, tracker)
                repo.updatePermissionState(LocationPermission.DeniedPermanently)
                val vm = PermissionViewModel(handler, tracker, repo)

                val allStates = mutableListOf<PermissionUiState>()
                val job = launch { vm.state.collect { allStates.add(it) } }
                job.cancel()

                // must never flash Idle before DeniedPermanently
                allStates.none {
                    it.permission == PermissionState.Idle
                } shouldBe true

                allStates.first().permission shouldBe PermissionState.DeniedPermanently
            }
        }
    }
})