package com.khalidtouch.classifiadmin.settings.navigation.profile

sealed interface ProfileScreenUiState {
    object Loading : ProfileScreenUiState
    data class Success(val data: ProfileData) : ProfileScreenUiState
}


data class ProfileData(
    val personalData: PersonalData?,
    val locationData: LocationData?,
)


data class PersonalData(
    val username: String,
    val phone: String,
    val bio: String,
    val dob: String,
) {
    companion object {
        val Default = PersonalData(
            username = "",
            phone = "",
            bio = "",
            dob = ""
        )
    }
}

data class LocationData(
    val address: String,
    val country: String,
    val stateOfCountry: String,
    val city: String,
    val postalCode: String,
) {
    companion object {
        val Default = LocationData(
            address = "",
            country = "",
            stateOfCountry = "",
            city = "",
            postalCode = ""
        )
    }
}