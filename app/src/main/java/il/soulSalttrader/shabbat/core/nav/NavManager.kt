package il.soulSalttrader.retro.core.nav

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NavManager @Inject constructor() {
    private val _commands = MutableSharedFlow<NavAction>(extraBufferCapacity = 1)
    val commands = _commands.asSharedFlow()

    private val _currentTarget = MutableStateFlow<NavTarget?>(value = null)
    val currentTarget = _currentTarget.asStateFlow()

    internal fun updateCurrentTarget(target: NavTarget?) {
        _currentTarget.value = target
    }

    fun navigateTo(target: NavTarget) = _commands.tryEmit(NavAction.To(target))
    fun navigateUp() = _commands.tryEmit(NavAction.Up)
    fun resetRoot(target: NavTarget) = _commands.tryEmit(NavAction.ResetTo(target))
    fun popTo(target: NavTarget) = _commands.tryEmit(NavAction.PopTo(target))
    fun popToRoot() = _commands.tryEmit(NavAction.PopToRoot)
}