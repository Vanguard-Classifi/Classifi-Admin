package com.vanguard.classifiadmin.data.network.models

import com.vanguard.classifiadmin.data.local.models.UserModel

data class UserNetworkModel(
    val userId: String? = null,
    var email: String? = null,
    val fullname: String? = null,
    var currentRole: String? = null,
    var roles: ArrayList<String> = arrayListOf(),
    var schoolIds: ArrayList<String> = arrayListOf(),
    var currentSchoolId: String? = null,
    var currentSchoolName: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var bio: String? = null,
    var dob: String?= null,
    var country: String? = null,
    var state: String? = null,
    var city: String? = null,
    var postalCode: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var classIds: ArrayList<String> = arrayListOf(),
    var lastModified: String? = null,
) {
    fun toLocal() = UserModel(
        userId = userId ?: "",
        email = email,
        fullname = fullname,
        currentRole = currentRole,
        roles = roles,
        phone = phone,
        country = country,
        state = state,
        city = city,
        bio = bio,
        dob = dob,
        postalCode = postalCode,
        schoolIds = schoolIds,
        currentSchoolId = currentSchoolId,
        currentSchoolName = currentSchoolName,
        address = address,
        studentIds = studentIds,
        classIds = classIds,
        lastModified = lastModified
    )
}