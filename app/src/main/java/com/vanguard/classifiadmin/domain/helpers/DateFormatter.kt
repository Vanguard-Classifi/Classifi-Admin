package com.vanguard.classifiadmin.domain.helpers

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.Locale

val DateModifiedFormat = "E, dd MM yyyy HH:mm"
val DateModifiedPatternComputational = DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm a")
val AssessmentDatePattern = "yyyy-MM-dd"
val SimpleDatePattern = "dd MMM, yyyy"
val AssessmentTimePattern = "HH:mm"
fun todayComputational(): String = DateModifiedPatternComputational.format(LocalDateTime.now())

@SuppressLint("SimpleDateFormat")
fun today(): String =
    SimpleDateFormat(DateModifiedFormat, Locale.getDefault()).format(Date())

fun getDefaultDateInMillis(): Long {
    val cal = Calendar.getInstance()
    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val date = cal.get(Calendar.DATE)
    cal.clear()
    cal.set(year, month, date)
    return cal.timeInMillis
}

fun String.minuteOffset(): Long {
    return try {
        val ref = LocalTime.parse(this, DateModifiedPatternComputational)
        val now = LocalTime.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.MINUTES)
    } catch (e: DateTimeParseException) {
        -1L
    }
}

fun String.hourOffset(): Long {
    return try {
        val ref = LocalTime.parse(this, DateModifiedPatternComputational)
        val now = LocalTime.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.HOURS)
    } catch (e: DateTimeParseException) {
        -1L
    }
}

fun String.dayOffset(): Long {
    return try {
        val ref = LocalDate.parse(this, DateModifiedPatternComputational)
        val now = LocalDate.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.DAYS)
    } catch (e: DateTimeParseException) {
        -1L
    }
}

fun String.weekOffset(): Long {
    return try {
        val ref = LocalDate.parse(this, DateModifiedPatternComputational)
        val now = LocalDate.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.WEEKS)
    } catch (e: DateTimeParseException) {
        -1L
    }
}

fun String.monthOffset(): Long {
    return try {
        val ref = LocalDate.parse(this, DateModifiedPatternComputational)
        val now = LocalDate.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.MONTHS)
    } catch (e: DateTimeParseException) {
        -1L
    }
}

fun String.yearOffset(): Long {
    return try {
        val ref = LocalDate.parse(this, DateModifiedPatternComputational)
        val now = LocalDate.parse(todayComputational(), DateModifiedPatternComputational)
        ref.until(now, ChronoUnit.YEARS)
    } catch (e: DateTimeParseException) {
        -1L
    }
}


fun String.lastModified(): String {
    val years = yearOffset()
    val months = monthOffset()
    val weeks = weekOffset()
    val days = dayOffset()
    val hours = hourOffset()
    val minutes = minuteOffset()

    if (years == 1L) return "A year ago"
    if (years > 1) return "$years years ago"
    if (months == 1L) return "A month ago"
    if (months > 1) return "$months months ago"
    if (weeks == 1L) return "A week ago"
    if (weeks > 1) return "$weeks weeks ago"
    if (days == 1L) return "Yesterday"
    if (days > 1) return "$days days ago"
    if (hours == 1L) return "An hour ago"
    if (hours > 1) return "$hours hours ago"
    if (minutes == 1L) return "A minute ago"
    if (minutes > 1) return "$minutes minutes ago"
    return "Just now"
}

fun String.toSimpleDate(): String {
    try {
        //get date string from assessment
        val date = SimpleDateFormat(AssessmentDatePattern, Locale.getDefault())
            .parse(this)
        //convert to date object
        return SimpleDateFormat(SimpleDatePattern, Locale.getDefault())
            .format(date!!)
        //format to new date pattern
    } catch (e: ParseException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return ""
}

fun main() {
    println("2023-03-26".toSimpleDate())
}

