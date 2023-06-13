package com.khalidtouch.classifiadmin.model.classifi

import com.khalidtouch.classifiadmin.model.UserAccount
import com.khalidtouch.classifiadmin.model.UserProfile
import com.khalidtouch.classifiadmin.model.UserRole
import java.time.LocalDateTime

data class ClassifiUser(
    val userId: Long? = null,
    var account: UserAccount? = null,
    var profile: UserProfile? = null,
    var dateCreated: LocalDateTime? = null,
    var joinedSchools: List<ClassifiSchool> = emptyList(),
    var joinedClasses: List<ClassifiClass> = emptyList(),
    val affiliatedUsers: List<ClassifiUser> = emptyList(),
) {

    val hasAdminPrivilege: Boolean
        get() = account?.userRole == UserRole.Admin || account?.userRole == UserRole.SuperAdmin

    val isATeacher: Boolean
        get() = account?.userRole == UserRole.Teacher

    val isAStudent: Boolean
        get() = account?.userRole == UserRole.Student

    val isAParent: Boolean
        get() = account?.userRole == UserRole.Parent

    val isAGuest: Boolean
        get() = account?.userRole == UserRole.Guest

    val alreadyAssignedToClass: Boolean
        get() = account?.userRole == UserRole.Student && joinedClasses.isNotEmpty()

    val alreadyAssignedToSchool: Boolean
        get() = account?.userRole == UserRole.Student && joinedSchools.isNotEmpty()
}