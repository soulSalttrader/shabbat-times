package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import il.soulSalttrader.shabbattimes.ui.effect.UiEffect
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Module
@InstallIn(ViewModelComponent::class)
object UiEffectModule {

    @Provides
    @ViewModelScoped
    fun provideUiEffects(): MutableSharedFlow<UiEffect> = MutableSharedFlow(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @Provides
    @ViewModelScoped
    fun provideUiEffectFlowAsSharedFlow(
        flow: MutableSharedFlow<UiEffect>
    ): SharedFlow<UiEffect> = flow
}