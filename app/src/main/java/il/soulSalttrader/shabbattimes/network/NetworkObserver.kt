package il.soulSalttrader.shabbattimes.network

import kotlinx.coroutines.flow.Flow

interface NetworkObserver {
    val isConnected: Flow<Boolean>
}