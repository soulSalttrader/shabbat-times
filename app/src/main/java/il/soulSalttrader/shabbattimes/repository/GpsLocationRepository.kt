package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface GpsLocationRepository {
    val location: Flow<Location?>
}