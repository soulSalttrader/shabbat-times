package il.soulSalttrader.shabbattimes.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import il.soulSalttrader.shabbattimes.settings.solarPosition.AstronomicalSolarDepressionCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.BinarySolarAltitudeSearch
import il.soulSalttrader.shabbattimes.settings.solarPosition.EphemerisBasedSolarPositionCalculator
import il.soulSalttrader.shabbattimes.settings.ephemeris.NoaaSolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarAltitudeSearch
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarDepressionTimeCalculator
import il.soulSalttrader.shabbattimes.settings.ephemeris.SolarEphemerisCalculator
import il.soulSalttrader.shabbattimes.settings.solarPosition.SolarPositionCalculator

@Module
@InstallIn(ViewModelComponent::class)
object TimeCalculatorModule {
    @Provides
    @ViewModelScoped
    fun provideEphemerisCalculator(): SolarEphemerisCalculator = NoaaSolarEphemerisCalculator()

    @Provides
    @ViewModelScoped
    fun providePositionCalculator(
        ephemerisCalculator: SolarEphemerisCalculator
    ): SolarPositionCalculator = EphemerisBasedSolarPositionCalculator(ephemerisCalculator)

    @Provides
    @ViewModelScoped
    fun provideAltitudeSearch(): SolarAltitudeSearch = BinarySolarAltitudeSearch()

    @Provides
    @ViewModelScoped
    fun provideTimeCalculator(
        positionCalculator: SolarPositionCalculator,
        altitudeSearch: SolarAltitudeSearch,
    ): SolarDepressionTimeCalculator =
        AstronomicalSolarDepressionCalculator(positionCalculator, altitudeSearch)
}