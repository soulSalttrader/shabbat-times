package il.soulSalttrader.shabbattimes.ui.viewModel

import app.cash.turbine.test
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.permission.PermissionTestFixture
import il.soulSalttrader.shabbattimes.ui.effect.PermissionSideEffectHandler
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import il.soulSalttrader.shabbattimes.ui.event.SearchEvent
import il.soulSalttrader.shabbattimes.ui.permission.PermissionUiState
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
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
        val repo = FakePermissionRepository()
        val effects = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1, onBufferOverflow = BufferOverflow.DROP_OLDEST)
        val handler = PermissionSideEffectHandler(repo, effects)
        val viewModel = PermissionViewModel(handler, repo)

        return PermissionTestFixture(viewModel, repo, effects)
    }

    describe("PERM_FRESH_S1 - SCENARIO: User grants permission on first ask") {
        it("PERM_FRESH_S1_VM - should reflect Granted after full Education → Requesting → Granted flow") {
            runTest {
                val (vm, repo, _) = setup()

                vm.state.test {
                    awaitItem() // initial Idle state

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().apply {
                        permission shouldBe PermissionState.Education
                        isDialogVisible shouldBe true
                    }

                    vm.dispatch(PermissionEvent.Request)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting

                    vm.dispatch(PermissionEvent.AllGranted)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Granted

                    repo.permissionState.value shouldBe LocationPermission.Granted
                }
            }
        }

        it("PERM_FRESH_S1_VM_2 - should keep isDialogVisible after repo emission following ShowEducation ") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // state after dispatch

                    // force a real emission by changing to something different first
                    repo.updatePermissionState(LocationPermission.Idle)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Idle emission

                    // now emit Education again - this is the repo emission we want to test
                    repo.updatePermissionState(LocationPermission.Education)
                    testDispatcher.scheduler.advanceUntilIdle()

                    awaitItem().isDialogVisible shouldBe true
                }
            }
        }
    }

    describe("PERM_FRESH_S2 - SCENARIO: User denies permission on first ask") {
        it("PERM_FRESH_S2_VM - should show Denied state after deny flow") {
            runTest {
                val (vm, repo, _) = setup()
                vm.state.test {
                    awaitItem() // initial Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Education - consume

                    vm.dispatch(PermissionEvent.Request)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Requesting - consume

                    vm.dispatch(PermissionEvent.DeniedWithRationale)
                    testDispatcher.scheduler.advanceUntilIdle()

                    // Denied - this is what we assert
                    awaitItem().permission shouldBe PermissionState.Denied
                    repo.permissionState.value shouldBe LocationPermission.Denied

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

                    vm.dispatch(PermissionEvent.DeniedWithRationale)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Denied - consume

                    vm.dispatch(PermissionEvent.AcceptedRationale)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting

                    vm.dispatch(PermissionEvent.AllGranted)
                    testDispatcher.scheduler.advanceUntilIdle()
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

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Education

                    vm.dispatch(PermissionEvent.Request)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // Requesting

                    vm.dispatch(PermissionEvent.DeniedWithRationale) // first deny
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Denied

                    vm.dispatch(PermissionEvent.AcceptedRationale) // tap Allow on rationale
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting

                    vm.dispatch(PermissionEvent.DeniedPermanently) // second deny
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    repo.permissionState.value shouldBe LocationPermission.DeniedPermanently

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_FRESH_S6 - SCENARIO: User dismisses education dialog") {
        it("PERM_FRESH_S6_VM - should return to Idle with dialog hidden when Education dialog dismissed") {
            runTest {
                val (vm, _, _) = setup()
                vm.state.test {
                    awaitItem()

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().apply {
                        permission shouldBe PermissionState.Education
                        isDialogVisible shouldBe true
                    }

                    vm.dispatch(PermissionEvent.DismissedRationale)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().apply {
                        permission shouldBe PermissionState.Education
                        isDialogVisible shouldBe false
                    }

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

                    vm.dispatch(PermissionEvent.DeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem() // DeniedPermanently

                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    testDispatcher.scheduler.advanceUntilIdle()

                    awaitItem().apply {
                        permission shouldBe PermissionState.Idle
                        isDialogVisible shouldBe false
                    }

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

                    vm.dispatch(PermissionEvent.DeniedPermanently)
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

                    vm.dispatch(PermissionEvent.DeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.ReturnedFromAppSettings)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Idle

                    vm.dispatch(PermissionEvent.ShowEducation)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().apply {
                        permission shouldBe PermissionState.Education
                        isDialogVisible shouldBe true
                    }

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

                    vm.dispatch(PermissionEvent.DeniedPermanently)
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

                    vm.dispatch(PermissionEvent.Request)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem()

                    vm.dispatch(PermissionEvent.DeniedPermanently)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    repo.permissionState.value shouldBe LocationPermission.DeniedPermanently

                    cancelAndIgnoreRemainingEvents()
                }
            }
        }
    }

    describe("PERM_RESTART_S1 - SCENARIO: Restart with granted permission") {
        it("PERM_RESTART_S1_VM_1 - should reflect Granted immediately on cold start ") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.Granted)
                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)
                val vm = PermissionViewModel(handler, repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.Granted
                        isDialogVisible shouldBe false
                    }
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
                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)
                val vm = PermissionViewModel(handler, repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.Denied
                        isDialogVisible shouldBe false
                    }
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
                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)
                val vm = PermissionViewModel(handler, repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.DeniedPermanently
                        isDialogVisible shouldBe false
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_RESTART_S3_VM_2 - should skip Education and show Open Settings after cold start when permission is permanently denied") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.DeniedPermanently) // persisted
                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)
                val vm = PermissionViewModel(handler, repo)

                vm.state.test {
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.DeniedPermanently

                    // user taps card
                    vm.dispatch(PermissionEvent.ShowDeniedPermanentlyDialog)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().apply {
                        permission shouldBe PermissionState.DeniedPermanently
                        isDialogVisible shouldBe true // Open Settings dialog, not Education
                    }

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
                    testDispatcher.scheduler.advanceUntilIdle()

                    val states = cancelAndConsumeRemainingEvents()
                        .filterIsInstance<app.cash.turbine.Event.Item<PermissionUiState>>()
                        .map { it.value }

                    // must never go back to Idle
                    states.none { it.permission == PermissionState.Idle } shouldBe true

                    // final state must be Education with dialog visible
                    states.last().apply {
                        permission shouldBe PermissionState.Education
                        isDialogVisible shouldBe true
                    }

                    // dialog must not have appeared more than once - isDialogVisible flips
                    // should only go true once, not true/false/true
                    states.count { it.isDialogVisible } shouldBe states.size // stays true throughout
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

                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)

                // simulate rotation — create new VM with same repo (same as config change)
                val recreatedVm = PermissionViewModel(handler, repo)

                recreatedVm.state.test {
                    awaitItem().permission shouldBe PermissionState.Education
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_EDGE_S2_VM_2 - should preserve DeniedPermanently state after configuration change") {
            runTest {
                val (vm, repo) = setup()
                vm.dispatch(PermissionEvent.DeniedPermanently)
                testDispatcher.scheduler.advanceUntilIdle()

                val (_, _, effects) = setup()
                val handler = PermissionSideEffectHandler(repo, effects)

                // simulate rotation — create new VM with same repo (same as config change)
                val recreatedVm = PermissionViewModel(handler, repo)

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
                vm.dispatch(PermissionEvent.Request)
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

                    vm.dispatch(PermissionEvent.Request)
                    testDispatcher.scheduler.advanceUntilIdle()
                    awaitItem().permission shouldBe PermissionState.Requesting

                    // ON_RESUME fires after returning from other app
                    // resolvePermissionEvent might return AllGranted or DeniedWithRationale
                    vm.dispatch(PermissionEvent.AllGranted)
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
                    vm.dispatch(SearchEvent.GpsLocationRequested)
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
                    val (_, _, effects) = setup()
                    val repo = FakePermissionRepository()
                    val handler = PermissionSideEffectHandler(repo, effects)
                    repo.updatePermissionState(locationPermission)
                    val vm = PermissionViewModel(handler, repo)

                    vm.state.test {
                        awaitItem().permission shouldBe expectedState
                        cancelAndIgnoreRemainingEvents()
                    }
                }
            }
        }
    }

    describe("PERM_COMBINE_S1 - SCENARIO: combine() fires twice causing invalid intermediate state") {
        it("PERM_COMBINE_S1_VM - should never produce Idle+dialogVisible intermediate state") {
            runTest(UnconfinedTestDispatcher()) {
                val (vm, _) = setup()
                val allStates = mutableListOf<PermissionUiState>()

                val job = launch { vm.state.collect { allStates.add(it) } }
                vm.dispatch(PermissionEvent.ShowEducation)

                job.cancel()

                allStates.none {
                    it.permission == PermissionState.Idle && it.isDialogVisible
                } shouldBe true

                allStates.last().permission shouldBe PermissionState.Education
                allStates.last().isDialogVisible shouldBe true
            }
        }
    }

    describe("PERM_COMBINE_S2 - SCENARIO: Idle flash on cold start with DeniedPermanently") {
        it("BUG_COMBINE_S2 - cold start with DeniedPermanently never flashes Idle first") {
            runTest(UnconfinedTestDispatcher()) {
                val (_, _, effects) = setup()
                val repo = FakePermissionRepository()
                val handler = PermissionSideEffectHandler(repo, effects)
                repo.updatePermissionState(LocationPermission.DeniedPermanently)
                val vm = PermissionViewModel(handler, repo)

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