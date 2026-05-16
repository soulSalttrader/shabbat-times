package il.soulSalttrader.shabbattimes.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.data.AppDatabase
import il.soulSalttrader.shabbattimes.data.CurrentLocationDao
import il.soulSalttrader.shabbattimes.data.SavedLocationDao
import jakarta.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "shabbat.db")
            .fallbackToDestructiveMigration(true) // TODO: replace with real migration before production release
            .build()

    @Provides
    fun provideSavedLocationDao(db: AppDatabase): SavedLocationDao = db.savedLocationDao()

    @Provides
    fun provideCurrentLocationDao(db: AppDatabase): CurrentLocationDao = db.currentLocationDao()
}