package il.soulSalttrader.shabbattimes.network.observer

import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.Test

class NetworkCallbackFlowTest  {

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `NETWORK_S4_2 - should emit distinct debounced values and replay last value to new subscribers`() = runTest {
        val sourceFlow = MutableSharedFlow<Boolean>(replay = 0)
        val fakeSource = object : ConnectivityFlowSource {
            override fun observe(): Flow<Boolean> = sourceFlow
        }

        val observer = NetworkConnectivityObserver(
            connectivityFlowSource = fakeSource,
            scope = backgroundScope,
        )

        val emissions = mutableListOf<Boolean>()
        val job = launch { observer.isConnected.toList(emissions) }
        runCurrent() // the collector subscribe before emitting

        sourceFlow.emit(true)
        sourceFlow.emit(true) // duplicate, filtered by distinctUntilChanged
        advanceTimeBy(301)
        sourceFlow.emit(false)
        advanceTimeBy(301)

        assertEquals(listOf(true, false), emissions)
        job.cancel()
    }
}