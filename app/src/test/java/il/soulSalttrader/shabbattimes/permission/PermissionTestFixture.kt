package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.settings.OneTimeMessageTracker
import il.soulSalttrader.shabbattimes.ui.viewModel.FakePermissionRepository
import il.soulSalttrader.shabbattimes.ui.viewModel.PermissionViewModel

data class PermissionTestFixture(
    val viewModel: PermissionViewModel,
    val repository: FakePermissionRepository,
    val tracker: OneTimeMessageTracker,
)
