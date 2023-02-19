package com.vanguard.classifiadmin.domain.helpers

import java.util.regex.Pattern


const val EMAIL_REGEX = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"

fun isPasswordValid(password: String): Boolean {
    return password.length >= 6
}

fun isEmailValid(email: String): Boolean {
    val emailPattern = Pattern.compile(EMAIL_REGEX)
    val formattedEmail = email.trim().lowercase()
    return emailPattern.matcher(formattedEmail).matches()
}