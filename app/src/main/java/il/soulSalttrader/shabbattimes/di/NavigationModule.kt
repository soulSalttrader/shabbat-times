package il.soulSalttrader.shabbattimes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.ui.nav.NavManager
import il.soulSalttrader.shabbattimes.ui.nav.Navigator
@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {
    @Binds
    fun bindNavigator(impl: NavManager): Navigator
}