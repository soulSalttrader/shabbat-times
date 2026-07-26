package il.soulSalttrader.shabbattimes.network.observer

import android.net.ConnectivityManager
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

/**
 * [ConnectivityManagerFlowSource] exists specifically to wrap the
 * [ConnectivityManager.networkCallbackFlow] extension function behind an
 * injectable interface ([ConnectivityFlowSource]), so that consumers like
 * [NetworkConnectivityObserver] can be unit tested with a plain fake instead
 * of needing static mocking themselves.
 *
 * This test verifies that thin wrapper's one job — delegation — which means
 * it's the one place that still needs to reach for [mockkStatic], since
 * [networkCallbackFlow] is a top-level extension function and can't be
 * mocked as a regular member via [mockk].
 *
 * `mockkStatic` is intentionally scoped to just this class rather than used
 * anywhere else in the codebase: it mutates shared JVM bytecode for the
 * whole test process, which makes it easy to leak state across unrelated
 * tests if used carelessly.
 *
 * Keeping it contained to this single delegation
 * test — with strict setUp/tearDown pairing — means the blast radius of
 * that risk is as small as possible, and every other test in the network
 * layer can rely on the [ConnectivityFlowSource] fake instead of touching
 * static mocking at all.
 */
class ConnectivityManagerFlowSourceTest {
    @Before
    fun setUp() {
        // Real Android framework extension function — can't be mocked as a
        // regular member call, so mockkStatic is the only option here.
        // Scoped narrowly to this one function reference (not the whole file)
        // to keep the static-mock surface as small as possible.
        mockkStatic(ConnectivityManager::networkCallbackFlow)
    }

    @After
    fun tearDown() {
        // Always unregister to avoid leaking this static mock into other
        // tests in the suite (mockkStatic is process-global, not test-scoped).
        unmockkStatic(ConnectivityManager::networkCallbackFlow)
    }

    @Test
    fun `NETWORK_S3_1 - should delegate to networkCallbackFlow extension on ConnectivityManager`() = runTest {
        val expectedFlow = flowOf(true, false)
        val connectivityManager = mockk<ConnectivityManager>()

        every { connectivityManager.networkCallbackFlow() } returns expectedFlow

        val source = ConnectivityManagerFlowSource(connectivityManager)

        assertEquals(listOf(true, false), source.observe().toList())
        verify(exactly = 1) {
            @Suppress("UnusedFlow")
            connectivityManager.networkCallbackFlow()
        }
    }
}