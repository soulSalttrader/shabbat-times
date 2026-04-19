package il.soulSalttrader.shabbattimes.repository

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    val location: Flow<Location?>
}