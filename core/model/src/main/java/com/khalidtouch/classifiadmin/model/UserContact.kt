package com.khalidtouch.classifiadmin.model

data class UserContact(
    var address: String? = null,
    var country: String? = null,
    var stateOfCountry: String? = null,
    var city: String? = null,
    var postalCode: String? = null,
)