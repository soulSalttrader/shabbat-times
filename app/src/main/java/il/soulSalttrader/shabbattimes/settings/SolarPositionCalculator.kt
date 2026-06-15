package il.soulSalttrader.shabbattimes.settings

import java.time.Instant

interface SolarPositionCalculator {
    fun altitudeDegrees(instant: Instant, latitudeDeg: Double, longitudeDeg: Double): Double
}