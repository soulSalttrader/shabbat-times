package il.soulSalttrader.shabbattimes.di

import jakarta.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class SolarTimesRetrofit

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class GeoapifyRetrofit

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope