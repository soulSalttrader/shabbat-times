package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import il.soulSalttrader.shabbattimes.repository.FakePermissionRepository
import il.soulSalttrader.shabbattimes.repository.PermissionRepository
import jakarta.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [PermissionRepositoryModule::class]
)
object FakePermissionRepositoryModule {

    val fakePermissionRepository = FakePermissionRepository()

    @Provides
    @Singleton
    fun providePermissionRepository(): PermissionRepository = fakePermissionRepository
}