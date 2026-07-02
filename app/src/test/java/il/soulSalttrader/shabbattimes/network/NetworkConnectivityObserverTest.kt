package il.soulSalttrader.shabbattimes.network

import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.NET_CAPABILITY_INTERNET
import android.net.NetworkCapabilities.NET_CAPABILITY_VALIDATED
import il.soulSalttrader.shabbattimes.network.observer.isOnline
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi

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
})