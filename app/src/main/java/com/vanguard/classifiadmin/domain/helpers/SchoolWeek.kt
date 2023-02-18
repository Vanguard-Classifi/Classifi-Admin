package com.vanguard.classifiadmin.domain.helpers

enum class SchoolWeek {
    Mon,
    Tues,
    Wed,
    Thurs,
    Fri,
}

fun String.toSchoolWeek(): SchoolWeek =
    when(this) {
        "Mon" -> SchoolWeek.Mon
        "Tues" -> SchoolWeek.Tues
        "Wed" -> SchoolWeek.Wed
        "Thurs" -> SchoolWeek.Thurs
        "Fri" -> SchoolWeek.Fri
        else -> SchoolWeek.Mon
    }