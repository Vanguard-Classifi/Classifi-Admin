package com.khalidtouch.chatme.database.converter

import androidx.room.TypeConverter
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.asUserRole

class UserRoleConverter {
    @TypeConverter
    fun roleToString(role: UserRole): String {
        return role.role
    }

    @TypeConverter
    fun stringToRole(role: String): UserRole {
        return role.asUserRole()
    }
}