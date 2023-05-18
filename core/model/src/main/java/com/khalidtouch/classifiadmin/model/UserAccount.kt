package com.khalidtouch.classifiadmin.model

data class UserAccount(
    val username: String,
    val email: String,
    val userRole: UserRole,
)