package il.soulSalttrader.shabbattimes.settings.solarPosition

import il.soulSalttrader.shabbattimes.settings.ephemeris.SolarEphemerisCalculator
import java.time.Instant
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin

class EphemerisBasedSolarPositionCalculator(
    private val ephemerisCalculator: SolarEphemerisCalculator,
) : SolarPositionCalculator {

    companion object {
        private const val SECONDS_PER_DAY = 86_400.0
        private const val MINUTES_PER_DAY = 1440.0
        private const val SOLAR_TIME_MINUTES_PER_DEGREE_LONGITUDE = 4.0
        private const val HOUR_ANGLE_DEGREES_PER_MINUTE_GROUP = 4.0
        private const val HOUR_ANGLE_OFFSET_DEGREES = 180.0
    }

    override fun altitudeDegrees(
        instant: Instant,
        latitudeDeg: Double,
        longitudeDeg: Double,
    ): Double {
        val ephemeris = ephemerisCalculator.ephemerisFor(instant)
        val trueSolarTimeMinutes = trueSolarTimeMinutes(instant, longitudeDeg, ephemeris.equationOfTimeMinutes)
        val hourAngleRad = hourAngleRadians(trueSolarTimeMinutes)

        return altitudeDegrees(
            latitudeDeg,
            ephemeris.declinationRad,
            hourAngleRad,
        )
    }

    private fun trueSolarTimeMinutes(
        instant: Instant,
        longitudeDeg: Double,
        equationOfTimeMinutes: Double
    ): Double {
        val utcMinutesOfDay = (instant.epochSecond % SECONDS_PER_DAY.toLong()) / 60.0

        return (utcMinutesOfDay
                + equationOfTimeMinutes
                + SOLAR_TIME_MINUTES_PER_DEGREE_LONGITUDE
                * longitudeDeg
        ) % MINUTES_PER_DAY
    }

    private fun hourAngleRadians(
        trueSolarTimeMinutes: Double
    ): Double {
        val quarterHourAngle = trueSolarTimeMinutes / HOUR_ANGLE_DEGREES_PER_MINUTE_GROUP

        val hourAngleDeg =
            when (quarterHourAngle < 0) {
                true -> quarterHourAngle + HOUR_ANGLE_OFFSET_DEGREES
                else -> quarterHourAngle - HOUR_ANGLE_OFFSET_DEGREES
            }

        return Math.toRadians(hourAngleDeg)
    }

    private fun altitudeDegrees(
        latitudeDeg: Double,
        declinationRad: Double,
        hourAngleRad: Double,
    ): Double {
        val latitudeRad = Math.toRadians(latitudeDeg)

        val altitudeRad =
            asin(
                sin(latitudeRad)
                    * sin(declinationRad)
                    + cos(latitudeRad)
                    * cos(declinationRad)
                    * cos(hourAngleRad)
            )

        return Math.toDegrees(altitudeRad)
    }
}