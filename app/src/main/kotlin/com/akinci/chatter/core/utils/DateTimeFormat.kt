package com.akinci.chatter.core.utils

import timber.log.Timber
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

enum class DateTimeFormat(
    private val formatter: DateTimeFormatter,
) {
    CLOCK_24(DateTimeFormatter.ofPattern("HH:mm")),
    STORE(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

    fun format(dateTime: ZonedDateTime?): String? = runCatching {
        formatter.format(dateTime)
    }.onFailure {
        Timber.e(it, "dateTime(ZonedDateTime) couldn't be formatted: $it")
    }.getOrNull()

    fun format(dateTime: String): String? = runCatching {
        formatter.format(ZonedDateTime.parse(dateTime))
    }.onFailure {
        Timber.e(it, "dateTime(String) couldn't be formatted: $it")
    }.getOrNull()
}
