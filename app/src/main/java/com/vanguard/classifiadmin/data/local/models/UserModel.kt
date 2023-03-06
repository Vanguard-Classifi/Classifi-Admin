package com.vanguard.classifiadmin.data.local.models

import com.vanguard.classifiadmin.data.network.models.UserNetworkModel

data class UserModel(
    val userId: String,
    var email: String? = null,
    var fullname: String? = null,
    var currentRole: String? = null,
    var profileImage: String? = null,
    var roles: ArrayList<String> = arrayListOf(),
    var schoolIds: ArrayList<String> = arrayListOf(),
    var currentSchoolId: String? = null,
    var currentSchoolName: String? = null,
    var password: String? = null,
    var address: String? = null,
    var phone: String? = null,
    var bio: String? = null,
    var dob: String?= null,
    var country: String? = null,
    var state: String? = null,
    var city: String? = null,
    var verified: Boolean? = null,
    var postalCode: String? = null,
    var studentIds: ArrayList<String> = arrayListOf(),
    var classIds: ArrayList<String> = arrayListOf(),
    var modifiedBy: String? = null,
    var lastModified: String? = null,
) {
    fun toNetwork() = UserNetworkModel(
        userId = userId,
        email = email,
        fullname = fullname,
        currentRole = currentRole,
        profileImage = profileImage,
        roles = roles,
        password = password,
        phone = phone,
        country = country,
        state = state,
        city = city,
        bio = bio,
        dob = dob,
        verified = verified,
        postalCode = postalCode,
        schoolIds = schoolIds,
        currentSchoolId = currentSchoolId,
        currentSchoolName = currentSchoolName,
        address = address,
        studentIds = studentIds,
        classIds = classIds,
        modifiedBy = modifiedBy,
        lastModified = lastModified
    )

    companion object  {
        val Invalid  = UserModel(
            userId = "Invalid",
        )
    }
}