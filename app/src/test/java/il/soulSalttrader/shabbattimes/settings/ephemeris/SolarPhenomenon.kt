package il.soulSalttrader.shabbattimes.settings.ephemeris

import java.time.Instant

data class SolarPhenomenon(
    val name: String,
    val instant: Instant,
    val expectedDeclinationDeg: Double,
    val expectedEquationOfTimeMinutes: Double,
    val tolerance: Double = 0.01,
)
