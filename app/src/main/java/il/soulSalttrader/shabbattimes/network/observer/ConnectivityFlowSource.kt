package il.soulSalttrader.shabbattimes.network.observer

import kotlinx.coroutines.flow.Flow

interface ConnectivityFlowSource {
    fun observe(): Flow<Boolean>
}
