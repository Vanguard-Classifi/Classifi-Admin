package com.khalidtouch.classifiadmin.model


enum class UserRole(val role: String) {
    SuperAdmin("Super Admin"),
    Admin("Admin"),
    Parent("Parent"),
    Teacher("Teacher"),
    Student("Student"),
    Guest("Guest"),
}


fun String?.asUserRole() = when (this) {
    null -> UserRole.Guest
    else -> UserRole.values()
        .firstOrNull { role -> role.role == this }
        ?: UserRole.Guest
}