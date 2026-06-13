package il.soulSalttrader.shabbattimes.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepositoryImpl
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class PermissionRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindPermissionRepository(impl: PermissionRepositoryImpl): PermissionRepository
}