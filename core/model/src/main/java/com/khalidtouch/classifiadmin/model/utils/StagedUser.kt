package com.khalidtouch.classifiadmin.model.utils

import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser

data class StagedUser(
    val user: ClassifiUser,
    val password: String,
    val confirmPassword: String,
)
