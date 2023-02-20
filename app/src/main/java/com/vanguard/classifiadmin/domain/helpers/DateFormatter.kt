package com.vanguard.classifiadmin.domain.helpers

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

val DateModifiedFormat = "E, dd MM yyyy HH:mm"

@SuppressLint("SimpleDateFormat")
fun today(): String =
    SimpleDateFormat(DateModifiedFormat, Locale.getDefault()).format(Date())