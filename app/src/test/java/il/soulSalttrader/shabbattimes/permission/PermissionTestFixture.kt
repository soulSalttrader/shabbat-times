package il.soulSalttrader.shabbattimes.permission

import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import il.soulSalttrader.shabbattimes.ui.viewModel.FakePermissionRepository
import il.soulSalttrader.shabbattimes.ui.viewModel.PermissionViewModel
import kotlinx.coroutines.flow.MutableSharedFlow

data class PermissionTestFixture(
    val viewModel: PermissionViewModel,
    val repository: FakePermissionRepository,
    val effects: MutableSharedFlow<UiEffect>
)
