package com.khalidtouch.classifiadmin.model

data class UserAccount(
    var username: String? = null,
    var email: String? = null,
    var userRole: UserRole? = null,
)