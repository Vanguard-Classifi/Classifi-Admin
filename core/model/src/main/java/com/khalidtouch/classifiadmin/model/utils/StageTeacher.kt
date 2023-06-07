package com.khalidtouch.classifiadmin.model.utils

import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser

data class StageTeacher(
    val teacher: ClassifiUser,
    val password: String,
    val confirmPassword: String,
)
