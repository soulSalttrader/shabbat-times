package il.soulSalttrader.shabbattimes.network.observer

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED

fun NetworkCapabilities?.isOnline(): Boolean {
    if (this == null) return false
    return hasCapability(NET_CAPABILITY_INTERNET) && hasCapability(NET_CAPABILITY_VALIDATED)
}