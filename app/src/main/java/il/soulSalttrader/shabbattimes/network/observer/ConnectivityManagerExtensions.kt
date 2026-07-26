package il.soulSalttrader.shabbattimes.network.observer

import android.net.ConnectivityManager
import android.net.ConnectivityManager.NetworkCallback
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkRequest
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun ConnectivityManager.networkCallbackFlow(): Flow<Boolean> = callbackFlow {
    trySend(getNetworkCapabilities(activeNetwork).isOnline()) // Seed

    val callback = object : NetworkCallback() {
        override fun onAvailable(network: Network) {
            trySend(getNetworkCapabilities(network).isOnline())
        }

        override fun onCapabilitiesChanged(
            network: Network,
            capabilities: NetworkCapabilities,
        ) {
            trySend(capabilities.isOnline())
        }

        override fun onLost(network: Network) {
            trySend(getNetworkCapabilities(activeNetwork).isOnline())
        }

        override fun onUnavailable() {
            trySend(false)
        }
    }

    val request = NetworkRequest.Builder()
        .addCapability(NET_CAPABILITY_INTERNET)
        .build()

    registerNetworkCallback(request, callback)
    awaitClose { unregisterNetworkCallback(callback) }
}