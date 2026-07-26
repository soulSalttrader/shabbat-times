# Date & Time Handling

## Core Principles

To ensure accuracy and avoid parsing issues, the following principles are followed

- All DTO fields (strings) are immediately converted into LocalDate and LocalTime domain objects.
- Calculations (± minutes) are performed only on strongly typed data.
- Formatting back to strings happens only at UI-binding time.

```kotlin
object DateTimeFormatters {
    const val API_DATE_PATTERN = "yyyy-MM-dd"
    const val TIME_24H = "HH:mm"
    const val TIME_12H = "h:mm a"

    val API_DATE_PARSER: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"
    val API_TIME_PARSER_24: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_TIME // "HH:mm:ss.SSS"
    val API_DATE_FORMATTER: DateTimeFormatter = DateTimeFormatter.ofPattern(API_DATE_PATTERN)
}
```

## Utility extensions

These utilities simplify common temporal operations, such as finding the next occurrence of a specific `DayOfWeek` or formatting dates for API consumption.

```kotlin
fun LocalDate.nextOrTodayDayOfWeek(target: DayOfWeek): LocalDate {
    val candidate = this.with(target)

    return when {
        candidate.isBefore(this) -> candidate.plusWeeks(1)
        else                     -> candidate
    }
}
```

- Parses [raw] as a time-of-day on [date] in [zoneOffset], rolling over to the next day if the result would fall before [referenceInstant] 
- (handles times that cross midnight, e.g. dusk after 00:00 when sunset is late in the evening).

```kotlin
fun parseRollingOver(
    raw: String,
    date: LocalDate,
    zoneOffset: ZoneOffset,
    referenceInstant: Instant?,
): Instant? {
    if (raw.isBlank()) return null
    val time = runCatching { LocalTime.parse(raw, API_TIME_PARSER_24) }.getOrNull() ?: return null
    val sameDay = date.atTime(time).toInstant(zoneOffset)

    val reference = referenceInstant ?: Instant.MIN
    return when (sameDay.isBefore(reference))  {
        true -> date.plusDays(1).atTime(time).toInstant(zoneOffset)
        else -> sameDay
    }
}
```

```kotlin
fun upcomingFriday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.FRIDAY)
fun upcomingSaturday(): LocalDate = now().nextOrTodayDayOfWeek(DayOfWeek.SATURDAY)
fun LocalDate.toApiDateString(): String = this.format(API_DATE_FORMATTER)
```
