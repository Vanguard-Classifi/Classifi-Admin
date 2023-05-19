package com.khalidtouch.chatme.database.models

import androidx.room.Embedded

data class RUserProfile(
    var profileImage: String? = null,
    var phone: String? = null,
    var bio: String? = null,
    var dob: String? = null,
    @Embedded var contact: RUserContact? = null,
)