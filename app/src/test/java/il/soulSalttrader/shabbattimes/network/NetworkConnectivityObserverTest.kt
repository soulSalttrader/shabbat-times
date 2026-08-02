package il.soulSalttrader.shabbattimes.network

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import il.soulSalttrader.shabbattimes.network.observer.ConnectivityFlowSource
import il.soulSalttrader.shabbattimes.network.observer.NetworkConnectivityObserver
import il.soulSalttrader.shabbattimes.network.observer.isOnline
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest

@OptIn(ExperimentalCoroutinesApi::class)
class NetworkConnectivityObserverTest : DescribeSpec({
    describe("NETWORK_1 - SCENARIO: NetworkCapabilities") {
        it("NETWORK_1_S1 - should return false if NetworkCapabilities is null") {
            val capabilities: NetworkCapabilities? = null
            capabilities.isOnline() shouldBe false
        }

        it("NETWORK_1_S2 - should return false if has NET_CAPABILITY_INTERNET but not NET_CAPABILITY_VALIDATED") {
            val capabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NET_CAPABILITY_INTERNET) } returns true
                every { hasCapability(NET_CAPABILITY_VALIDATED) } returns false
            }
            capabilities.isOnline() shouldBe false
        }

        it("NETWORK_1_S3 - should return false if has NET_CAPABILITY_VALIDATED but not NET_CAPABILITY_INTERNET") {
            val capabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NET_CAPABILITY_INTERNET) } returns false
                every { hasCapability(NET_CAPABILITY_VALIDATED) } returns true
            }
            capabilities.isOnline() shouldBe false
        }

        it("NETWORK_1_S4 - should return true if has NET_CAPABILITY_VALIDATED and NET_CAPABILITY_INTERNET") {
            val capabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NET_CAPABILITY_INTERNET) } returns true
                every { hasCapability(NET_CAPABILITY_VALIDATED) } returns true
            }
            capabilities.isOnline() shouldBe true
        }

        it("NETWORK_1_S5 - should return false if has neither NET_CAPABILITY_VALIDATED nor NET_CAPABILITY_INTERNET") {
            val capabilities = mockk<NetworkCapabilities> {
                every { hasCapability(NET_CAPABILITY_INTERNET) } returns false
                every { hasCapability(NET_CAPABILITY_VALIDATED) } returns false
            }
            capabilities.isOnline() shouldBe false
        }
    }

    describe("NETWORK_2 - SCENARIO: NetworkConnectivityObserver: WhileSubscribed(5000)") {
        it("NETWORK_2_1 - should stop collecting from source after last subscriber unsubscribes and WhileSubscribed timeout elapses") {
            runTest {
                var upstreamActive = false
                val sourceFlow = flow {
                    upstreamActive = true
                    try {
                        emitAll(MutableSharedFlow<Boolean>(replay = 0))
                    } finally {
                        upstreamActive = false
                    }
                }
                val fakeSource = object : ConnectivityFlowSource {
                    override fun observe(): Flow<Boolean> = sourceFlow
                }

                val observer = NetworkConnectivityObserver(fakeSource, backgroundScope)

                val job = launch { observer.isConnected.collect { } }
                runCurrent()
                upstreamActive shouldBe true

                job.cancel()
                runCurrent()
                upstreamActive shouldBe true // still active — WhileSubscribed(5000) grace period

                advanceTimeBy(5001)
                upstreamActive shouldBe false // now unsubscribed upstream
            }
        }
    }
})
