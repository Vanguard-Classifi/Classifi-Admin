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

fun String?.orDefaultImageUrl() = this ?: "file://dev/null"


