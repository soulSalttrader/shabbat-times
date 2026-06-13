package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import il.soulSalttrader.shabbattimes.ui.effect.PermissionSideEffectHandler
import il.soulSalttrader.shabbattimes.ui.effect.SideEffectHandler
import il.soulSalttrader.shabbattimes.ui.event.PermissionEvent

@Module
@InstallIn(ViewModelComponent::class)
object PermissionModule {

    @Provides
    @ViewModelScoped
    fun providePermissionSideEffectHandler(
        handler: PermissionSideEffectHandler
    ): SideEffectHandler<PermissionEvent> = handler
}