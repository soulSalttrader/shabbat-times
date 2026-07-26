package il.soulSalttrader.shabbattimes.network.observer

import android.net.ConnectivityManager
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ConnectivityManagerExtensionsTest {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_S4_1 - should unregister network callback when flow collection is cancelled`() = runTest {
        val connectivityManager = mockk<ConnectivityManager>(relaxed = true)
        every { connectivityManager.activeNetwork } returns null
        every { connectivityManager.getNetworkCapabilities(any()) } returns null

        val job = launch {
            connectivityManager.networkCallbackFlow().collect { }
        }

        runCurrent()
        verify(exactly = 1) {
            connectivityManager.registerNetworkCallback(any(), any<ConnectivityManager.NetworkCallback>())
        }

        job.cancel()
        job.join()

        verify(exactly = 1) {
            connectivityManager.unregisterNetworkCallback(any<ConnectivityManager.NetworkCallback>())
        }
    }
}