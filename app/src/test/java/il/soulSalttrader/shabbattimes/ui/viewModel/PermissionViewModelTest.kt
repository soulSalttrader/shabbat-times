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
    }
})