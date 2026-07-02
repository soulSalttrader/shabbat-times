package il.soulSalttrader.shabbattimes.network

import il.soulSalttrader.shabbattimes.network.observer.NetworkObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeNetworkObserver(initialConnected: Boolean = true) : NetworkObserver {
    private val _isConnected = MutableSharedFlow<Boolean>(replay = 1)
    override val isConnected: Flow<Boolean> = _isConnected

    init {
        _isConnected.tryEmit(initialConnected)
    }

    suspend fun setConnected(connected: Boolean) {
        _isConnected.emit(connected)
    }
}