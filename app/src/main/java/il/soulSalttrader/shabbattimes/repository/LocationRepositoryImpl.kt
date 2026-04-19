package il.soulSalttrader.shabbattimes.repository

import android.annotation.SuppressLint
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.shareIn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val fusedClient: FusedLocationProviderClient,
    private val scope: CoroutineScope,
    private val priority: Int = Priority.PRIORITY_HIGH_ACCURACY,
    private val interval: Long = 1L,
    private val minDistance: Float = 1000f,
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override val location: Flow<Location?> = callbackFlow {
        val request = LocationRequest.Builder(priority, interval)
            .setMinUpdateDistanceMeters(minDistance)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                trySend(result.locations.lastOrNull())
            }
        }

        fusedClient.requestLocationUpdates(
            request,
            callback,
            Looper.getMainLooper()
        )

        awaitClose { fusedClient.removeLocationUpdates(callback) }
    }.shareIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        replay = 1
    )
}