package il.soulSalttrader.shabbattimes.content

sealed interface Input<out T> {
    data object Idle : Input<Nothing>
    data object Empty : Input<Nothing>
    data class Value<T>(val value: T) : Input<T>
}