package com.vanguard.classifiadmin.domain.extensions

fun String.splitWithSpace() = split(" ")

fun String.splitWithComma() = split(",")

fun String?.orStudent(): String {
    return this ?: "Student"
}

fun String?.orParent(): String {
    return this ?: "Parent"
}

fun String?.orTeacher(): String {
    return this ?: "Teacher"
}

fun String?.orZeroString(): String {
    return this ?: "0"
}