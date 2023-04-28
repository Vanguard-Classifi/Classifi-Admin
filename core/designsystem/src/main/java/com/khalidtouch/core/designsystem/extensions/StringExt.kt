package com.khalidtouch.core.designsystem.extensions

fun String.splitWithSpace() = split(" ")

fun String.splitWithComma() = split(",")

fun String.getInitials(): String {
    val split = this.uppercase().splitWithSpace()
    var result = mutableListOf<String>()
    return try {
        split.forEachIndexed { _, item ->
            result.add(item[0].toString())
        }
        result.joinToString(separator = "")
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
