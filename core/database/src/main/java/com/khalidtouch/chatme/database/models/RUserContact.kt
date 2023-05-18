package com.khalidtouch.chatme.database.models

import com.khalidtouch.classifiadmin.model.UserContact

data class RUserContact(
    var address: String? = null,
    var country: String? = null,
    var stateOfCountry: String? = null,
    var city: String? = null,
    var postalCode: String? = null,
)