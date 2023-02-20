package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.UserNetworkModel

data class UserModel(
    val userId: String,
    var email: String? = null,
    val fullname: String? = null,
    var currentRole: String? = null,
    var roles: ArrayList<String> = arrayListOf(),
    var schoolIds: ArrayList<String> = arrayListOf(),
    var currentSchoolId: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var classIds: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
) {
    fun toNetwork() = UserNetworkModel(
        userId = userId,
        email = email,
        fullname = fullname,
        currentRole = currentRole,
        roles = roles,
        phone = phone,
        schoolIds = schoolIds,
        currentSchoolId = currentSchoolId,
        address = address,
        studentIds = studentIds,
        classIds = classIds,
        lastModified = lastModified
    )
}