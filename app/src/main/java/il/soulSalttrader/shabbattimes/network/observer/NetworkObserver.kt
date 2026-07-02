package il.soulSalttrader.shabbattimes.network.observer

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    val isConnected: Flow<Boolean>
}