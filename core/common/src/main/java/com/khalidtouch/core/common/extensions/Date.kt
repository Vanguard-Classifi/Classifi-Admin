package com.khalidtouch.core.common.extensions

import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.TimeZone


fun Long?.asLocalDate(): LocalDate {
    if (this == null) return LocalDate.MIN
    return LocalDateTime.ofInstant(Instant.ofEpochMilli(this), TimeZone.getDefault().toZoneId())
        .toLocalDate() ?: LocalDate.MIN
}

fun LocalDate.asDateString(): String =
    format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))