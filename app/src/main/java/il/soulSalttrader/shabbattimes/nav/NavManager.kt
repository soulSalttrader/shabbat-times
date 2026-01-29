package il.soulSalttrader.shabbattimes.nav

import androidx.navigation.NavOptionsBuilder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavManager @Inject constructor() : Navigator {
    private val _commands = MutableSharedFlow<NavAction>(extraBufferCapacity = 1)
    override val commands = _commands.asSharedFlow()

    private val _currentTarget = MutableStateFlow<NavTarget?>(value = null)
    override val currentTarget = _currentTarget.asStateFlow()

    override fun updateCurrentTarget(target: NavTarget?) {
        _currentTarget.value = target
    }

    override fun navigateTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.To(target, navOptions))

    override fun resetRoot(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.ResetTo(target, navOptions))

    override fun popTo(
        target: NavTarget,
        navOptions: NavOptionsBuilder.() -> Unit
    ) = _commands.tryEmit(NavAction.PopTo(target, navOptions))

    override fun navigateUp() = _commands.tryEmit(NavAction.Up)
    override fun popToRoot() = _commands.tryEmit(NavAction.PopToRoot)
}