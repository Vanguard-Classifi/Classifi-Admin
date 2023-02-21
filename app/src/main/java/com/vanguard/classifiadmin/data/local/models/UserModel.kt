package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.UserNetworkModel

data class UserModel(
    val userId: String,
    var email: String? = null,
    var fullname: String? = null,
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
    fun toNetwork() = UserNetworkModel(
        userId = userId,
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

    companion object  {
        val Invalid  = UserModel(
            userId = "Invalid",
        )
    }
}