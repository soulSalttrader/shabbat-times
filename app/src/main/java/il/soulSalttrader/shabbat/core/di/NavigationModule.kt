package il.soulSalttrader.retro.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.retro.core.nav.NavManager
import il.soulSalttrader.retro.core.nav.Navigator

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    fun bindNavigator(impl: NavManager): Navigator
}