package il.soulSalttrader.shabbattimes.common.constants

import com.google.android.gms.location.Priority

object GpsConfig {
    const val PRIORITY = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    const val INTERVAL_MS = 30_000L
    const val MIN_DISTANCE_METERS = 1000f
}