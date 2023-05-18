package com.khalidtouch.classifiadmin.model.classifi

import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserProfile
import com.khalidtouch.classifiadmin.model.UserRole
import kotlinx.datetime.LocalDateTime
import java.util.Date


data class ClassifiUser(
    val userId: Long,
    val account: UserAccount,
    val profile: UserProfile,
    val dateCreated: LocalDateTime,
    val joinedSchools: List<ClassifiSchool>,
    val joinedClasses: List<ClassifiClass>,
) {
    val hasAdminPrivilege: Boolean
        get() = account.userRole == UserRole.Admin || account.userRole == UserRole.SuperAdmin

    val isATeacher: Boolean
        get() = account.userRole == UserRole.Teacher

    val isAStudent: Boolean
        get() = account.userRole == UserRole.Student

    val isAParent: Boolean
        get() = account.userRole == UserRole.Parent

    val isAGuest: Boolean
        get() = account.userRole == UserRole.Guest

    val alreadyAssignedToClass: Boolean
        get() = account.userRole == UserRole.Student && joinedClasses.isNotEmpty()

    val alreadyAssignedToSchool: Boolean
        get() = account.userRole == UserRole.Student && joinedSchools.isNotEmpty()
}