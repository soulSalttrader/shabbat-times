package il.soulSalttrader.shabbattimes.ui.viewModel

import app.cash.turbine.test
import il.soulSalttrader.shabbattimes.model.LocationPermission
import il.soulSalttrader.shabbattimes.permission.PermissionState
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
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

    fun setup(): Pair<PermissionViewModel, FakePermissionRepository> {
        val repo = FakePermissionRepository()
        val vm = PermissionViewModel(repo)
        return vm to repo
    }

    describe("PERM_FRESH — fresh install flow") {

        // PERM_FRESH_S1 — Grant on first ask
        it("PERM_FRESH_S1 — full happy path: Education → Requesting → Granted") {
            runTest {
                val (vm, repo) = setup()

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

        it("PERM_FRESH_S1: isDialogVisible must survive repo emission after ShowEducation") {
            runTest {
                val (vm, repo) = setup()
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

        it("PERM_FRESH_S2 — deny → rationale visible, no GPS card") {
            runTest {
                val (vm, repo) = setup()
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

        // PERM_FRESH_S3 — Deny then allow via rationale
        it("PERM_FRESH_S3 — deny → accept rationale → grant") {
            runTest {
                val (vm, repo) = setup()
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

        //
        it("PERM_FRESH_S4 — deny twice → permanently denied") {
            runTest {
                val (vm, repo) = setup()
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

    describe("PERM_SETTINGS - App Settings Flow") {

        it("PERM_SETTINGS_S1 - ReturnedFromAppSettings resets DeniedPermanently to Idle") {
            runTest {
                val (vm, repo) = setup()

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

        it("PERM_SETTINGS_S1 - After returning from Settings, tapping card should start fresh Education flow") {
            runTest {
                val (vm, _) = setup()

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

        it("PERM_SETTINGS_S2 - User grants in Settings → next tap grants") {
            runTest {
                val (vm, repo) = setup()
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

        it("PERM_SETTINGS_S3 - ignore settings → return → Idle → re-denied on next request") {
            runTest {
                val (vm, repo) = setup()
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

    describe("PERM_RESTART - App Restart Scenarios") {

        // PERM_RESTART_S1 — Restart with granted permission
        it("PERM_RESTART_S1 - cold start with Granted, no dialogs") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.Granted)
                val vm = PermissionViewModel(repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.Granted
                        isDialogVisible shouldBe false
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        // PERM_RESTART_S2 — Restart with denied permission
        it("PERM_RESTART_S2 - cold start with Denied, no dialogs") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.Denied)
                val vm = PermissionViewModel(repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.Denied
                        isDialogVisible shouldBe false
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        // PERM_RESTART_S3 - Restart with permanently denied
        it("PERM_RESTART_S3 - cold start with DeniedPermanently, no dialogs") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.DeniedPermanently)
                val vm = PermissionViewModel(repo)

                vm.state.test {
                    awaitItem().apply {
                        permission shouldBe PermissionState.DeniedPermanently
                        isDialogVisible shouldBe false
                    }
                    cancelAndIgnoreRemainingEvents()
                }
            }
        }

        it("PERM_RESTART_S3 - cold start after permanent denial skips Education, shows Open Settings on tap") {
            runTest {
                val repo = FakePermissionRepository()
                repo.updatePermissionState(LocationPermission.DeniedPermanently) // persisted
                val vm = PermissionViewModel(repo)

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

        // PERM_RESTART_S4 - External revocation (e.g. settings revoke while app was backgrounded)
        it("PERM_RESTART_S4 - repo emits Denied after external revocation") {
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

    }
})