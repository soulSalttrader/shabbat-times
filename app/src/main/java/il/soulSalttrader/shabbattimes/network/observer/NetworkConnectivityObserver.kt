package il.soulSalttrader.shabbattimes.network.observer

import il.soulSalttrader.shabbattimes.di.ApplicationScope
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.shareIn

@Singleton
class NetworkConnectivityObserver @Inject constructor(
    connectivityFlowSource: ConnectivityFlowSource,
    @param:ApplicationScope private val scope: CoroutineScope,
) : NetworkObserver {

    @OptIn(FlowPreview::class)
    override val isConnected: Flow<Boolean> = connectivityFlowSource
        .observe()
        .distinctUntilChanged()
        .debounce(300L)
        .shareIn(
            scope = scope,
            started = WhileSubscribed(5000),
            replay = 1,
        )
}