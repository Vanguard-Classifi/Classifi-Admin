package com.vanguard.classifiadmin.domain.helpers

enum class SchoolWeek {
    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
}

fun String.toSchoolWeek(): SchoolWeek =
    when(this) {
        "Monday" -> SchoolWeek.Monday
        "Tuesday" -> SchoolWeek.Tuesday
        "Wednesday" -> SchoolWeek.Wednesday
        "Thursday" -> SchoolWeek.Thursday
        "Friday" -> SchoolWeek.Friday
        else -> SchoolWeek.Monday
    }