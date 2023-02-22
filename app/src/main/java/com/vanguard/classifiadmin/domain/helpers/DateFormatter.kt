package com.vanguard.classifiadmin.domain.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

val DateModifiedFormat = "E, dd MM yyyy HH:mm"

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