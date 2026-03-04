package il.soulSalttrader.shabbattimes.content

sealed interface Selection<out T> {
    data object Idle : Selection<Nothing>
    data object None : Selection<Nothing>
    data class Selected<T>(val value: T) : Selection<T>
}