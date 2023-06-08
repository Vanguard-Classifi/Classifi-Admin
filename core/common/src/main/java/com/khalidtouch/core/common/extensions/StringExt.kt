package com.khalidtouch.core.common.extensions

import java.util.regex.Pattern


const val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"


fun String.isPasswordValid(): Boolean {
    return this.length >= 6
}

fun String.isEmailValid(): Boolean {
    val emailPattern = Pattern.compile(EMAIL_REGEX)
    val formattedEmail = this.trim().lowercase()
    return emailPattern.matcher(formattedEmail).matches()
}

fun String?.orDefaultImageUrl() = when(this) {
    null -> "file://dev/null"
    "" -> "file://dev/null"
    else -> this
}

fun String?.ifNullOrBlank(default: String) = when (this) {
    null -> default
    "" -> default
    else -> this
}