package il.soulSalttrader.shabbattimes.common.constants

import com.google.android.gms.location.Priority
import il.soulSalttrader.shabbattimes.Debug

object GpsConfig {
    val PRIORITY = if (Debug.enabled) Priority.PRIORITY_HIGH_ACCURACY else Priority.PRIORITY_BALANCED_POWER_ACCURACY
    val INTERVAL_MS = if (Debug.enabled) 1000L else 30_000L
    const val MIN_DISTANCE_METERS = 1000f
}