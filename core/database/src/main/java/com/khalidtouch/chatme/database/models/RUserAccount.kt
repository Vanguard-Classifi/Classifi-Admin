package com.khalidtouch.chatme.database.models

import androidx.room.Embedded
import com.khalidtouch.classifiadmin.model.UserRole


data class RUserAccount(
    val username: String? = null,
    val email: String? = null,
    val userRole: UserRole,
)