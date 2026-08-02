package il.soulSalttrader.shabbattimes.model

data class HalachicTimes(
    val coordinates: Coordinates,
    val candleLighting: TimeState,
    val havdalah: TimeState,
)
