package com.khalidtouch.classifiadmin.model.utils

data class CreateAccountData(
    val email: String,
    val password: String,
    val confirmPassword: String,
)