package il.soulSalttrader.shabbattimes.model

import java.time.LocalDate
import java.time.LocalTime

sealed interface TimeState {
    open class Available(open val time: LocalTime, open val date: LocalDate) : TimeState
    open class Unavailable(val reason: Any? = null) : TimeState
}