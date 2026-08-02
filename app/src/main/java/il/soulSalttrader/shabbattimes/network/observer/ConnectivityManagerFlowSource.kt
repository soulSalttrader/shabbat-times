package il.soulSalttrader.shabbattimes.network.observer

import android.net.ConnectivityManager
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class ConnectivityManagerFlowSource @Inject constructor(
    private val connectivityManager: ConnectivityManager,
) : ConnectivityFlowSource {
    override fun observe(): Flow<Boolean> = connectivityManager.networkCallbackFlow()
}
