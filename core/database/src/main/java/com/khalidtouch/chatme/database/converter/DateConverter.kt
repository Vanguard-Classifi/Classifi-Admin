package com.khalidtouch.chatme.database.converter

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDateTime

class DateConverter {
    @TypeConverter
    fun dateToString(date: LocalDateTime?): String? = date?.toString()

    @TypeConverter
    fun stringToDate(date: String?): LocalDateTime? = date?.let { LocalDateTime.parse(it) }
}