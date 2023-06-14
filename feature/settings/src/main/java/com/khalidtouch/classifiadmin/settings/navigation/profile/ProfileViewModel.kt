package com.khalidtouch.classifiadmin.settings.navigation.profile

import android.content.Context
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.settings.R
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    @ApplicationContext context: Context,
) : ViewModel() {
    private val currentUser: StateFlow<ClassifiUser?> = userDataRepository.userData.map {
        val userId = it.userId
        val currentUser = userRepository.fetchUserById(userId)
        currentUser
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = null,
    )

    private val personalData: StateFlow<PersonalData> = currentUser.map {
        PersonalData(
            username = it?.account?.username ?: context.getString(R.string.click_to_add_username),
            phone = it?.profile?.phone ?: context.getString(R.string.click_to_add_phone),
            bio = it?.profile?.bio ?: context.getString(R.string.click_to_add_bio),
            dob = it?.profile?.dob ?: context.getString(R.string.click_to_add_dob),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = PersonalData.Default
    )


    private val locationData: StateFlow<LocationData> = currentUser.map {
        LocationData(
            address = it?.profile?.contact?.address ?: context.getString(R.string.click_to_add_address),
            country = it?.profile?.contact?.country ?: context.getString(R.string.click_to_add_country),
            stateOfCountry = it?.profile?.contact?.stateOfCountry ?: context.getString(R.string.click_to_add_state),
            city = it?.profile?.contact?.city ?: context.getString(R.string.click_to_add_city),
            postalCode = it?.profile?.contact?.postalCode ?: context.getString(R.string.click_to_add_postal_code),
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = LocationData.Default,
    )

    val uiState: StateFlow<ProfileScreenUiState> = combine(
        locationData,
        personalData,
    ) { location, personal ->
        ProfileScreenUiState.Success(
            data = ProfileData(
                personalData = personal,
                locationData = location,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ProfileScreenUiState.Loading
    )
}
