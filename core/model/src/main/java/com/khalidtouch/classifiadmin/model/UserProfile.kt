package com.khalidtouch.classifiadmin.model


data class UserProfile(
    var profileImage: String? = null,
    var username: String? = null,
    var phone: String? = null,
    var bio: String? = null,
    var dob: String? = null,
    var contact: UserContact? = null,
)