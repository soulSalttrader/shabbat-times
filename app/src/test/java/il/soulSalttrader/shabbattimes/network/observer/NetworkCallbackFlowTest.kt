package il.soulSalttrader.shabbattimes.network.observer

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import androidx.test.core.app.ApplicationProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.Shadows
import org.robolectric.annotation.Config
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest

private fun ConnectivityManager.shadow() = Shadows.shadowOf(this)

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33])
class NetworkCallbackFlowTest {

    private lateinit var connectivityManager: ConnectivityManager

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        connectivityManager = context.getSystemService(ConnectivityManager::class.java)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_CONNECT_3_S1 - initial seed value reflects activeNetwork's capabilities at subscription time`() = runTest {
        val values = mutableListOf<Boolean>()
        val job = launch { connectivityManager.networkCallbackFlow().collect { values.add(it) } }

        advanceUntilIdle()
        assertEquals(listOf(false), values)
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_CONNECT_3_S2 - onAvailable emits based on that network's capabilities`() = runTest {
        val shadow = connectivityManager.shadow()
        val values = mutableListOf<Boolean>()
        val job = launch { connectivityManager.networkCallbackFlow().collect { values.add(it) } }
        advanceUntilIdle()

        val network = mockk<Network>()
        val capabilities = mockk<NetworkCapabilities> {
            every { hasCapability(NET_CAPABILITY_INTERNET) } returns true
            every { hasCapability(NET_CAPABILITY_VALIDATED) } returns true
        }
        shadow.setNetworkCapabilities(network, capabilities)
        shadow.networkCallbacks.last().onAvailable(network)

        advanceUntilIdle()
        assertTrue(values.last())
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_CONNECT_3_S3 - onCapabilitiesChanged emits based on the new capabilities directly, not a re-fetch`() = runTest {
        val shadow = connectivityManager.shadow()
        val values = mutableListOf<Boolean>()
        val job = launch { connectivityManager.networkCallbackFlow().collect { values.add(it) } }
        advanceUntilIdle()

        val capabilities = mockk<NetworkCapabilities> {
            every { hasCapability(NET_CAPABILITY_INTERNET) } returns true
            every { hasCapability(NET_CAPABILITY_VALIDATED) } returns true
        }
        shadow.networkCallbacks.last().onCapabilitiesChanged(mockk(), capabilities)

        advanceUntilIdle()
        assertTrue(values.last())
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_CONNECT_3_S4 - onLost re-queries activeNetwork, reflecting another active network if present`() = runTest {
        val shadow = connectivityManager.shadow()
        val values = mutableListOf<Boolean>()
        val job = launch { connectivityManager.networkCallbackFlow().collect { values.add(it) } }
        advanceUntilIdle()

        shadow.networkCallbacks.last().onLost(mockk())

        advanceUntilIdle()
        assertFalse(values.last())
        job.cancel()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_CONNECT_3_S5 - onUnavailable emits false unconditionally`() = runTest {
        val shadow = connectivityManager.shadow()
        val values = mutableListOf<Boolean>()
        val job = launch { connectivityManager.networkCallbackFlow().collect { values.add(it) } }
        advanceUntilIdle()

        shadow.networkCallbacks.last().onUnavailable()

        advanceUntilIdle()
        assertFalse(values.last())
        job.cancel()
    }
}
