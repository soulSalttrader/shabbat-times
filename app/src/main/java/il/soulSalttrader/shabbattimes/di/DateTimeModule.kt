package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import il.soulSalttrader.shabbattimes.model.ShabbatCalendar
import jakarta.inject.Singleton
import java.time.LocalDate

@Module
@InstallIn(SingletonComponent::class)
object DateTimeModule {

    @Provides
    @Singleton
    fun provideShabbatCalendar(): ShabbatCalendar = object : ShabbatCalendar {
        override fun upcomingCandleLightingDate(): LocalDate = upcomingCandleLightingDate()
        override fun upcomingHavdalahDate(): LocalDate = upcomingHavdalahDate()
    }
}