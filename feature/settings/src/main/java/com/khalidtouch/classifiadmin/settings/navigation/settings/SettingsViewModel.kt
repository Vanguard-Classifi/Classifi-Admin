package com.khalidtouch.classifiadmin.settings.navigation.settings

import android.util.Log
import androidx.compose.foundation.gestures.DraggableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.repository.UserRepository
import com.khalidtouch.chatme.domain.usecases.GetCurrentUserUseCase
import com.khalidtouch.classifiadmin.data.util.ReadCountriesPagingSource
import com.khalidtouch.classifiadmin.model.DarkThemeConfig
import com.khalidtouch.classifiadmin.model.UserProfile
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.classifiadmin.settings.navigation.profile.LocationData
import com.khalidtouch.classifiadmin.settings.navigation.profile.PersonalData
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    readCountriesPagingSource: ReadCountriesPagingSource,
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
) : ViewModel() {

    val TAG = "SettingsVM"
    private val _selectedTabIndex: MutableLiveData<Int> = MutableLiveData(0)
    val selectedTabIndex: LiveData<Int> = _selectedTabIndex
    val tabs = Settings.values().toList()
    var isSwipeLeft = false
    private val draggableState = DraggableState {
        isSwipeLeft = it > 0
    }

    val countryPagingSource = Pager(PagingConfig(pageSize = 20)) {
        readCountriesPagingSource
    }.flow

    private val _currentSettingItemClicked: MutableLiveData<SettingItemClicked> =
        MutableLiveData(SettingItemClicked.None)
    val currentSettingItemClicked: LiveData<SettingItemClicked> = _currentSettingItemClicked

    private var _dragState = MutableLiveData<DraggableState>(draggableState)
    val dragState: LiveData<DraggableState> = _dragState

    private val _username = MutableStateFlow<String>("")
    private val _phone = MutableStateFlow<String>("")
    private val _bio = MutableStateFlow<String>("")
    private val _dob = MutableStateFlow<String>("")
    private val _address = MutableStateFlow<String>("")
    private val _country = MutableStateFlow<String>("")
    private val _stateOfCountry = MutableStateFlow<String>("")
    private val _city = MutableStateFlow<String>("")
    private val _postalCode = MutableStateFlow<String>("")
    private val _hasUserProfileUpdated = MutableStateFlow<Boolean>(false)
    private val _darkThemeConfigDialog = MutableStateFlow<Boolean>(false)
    val darkThemeConfigDialog = _darkThemeConfigDialog

    private val locationData: StateFlow<LocationData> =
        combine(
            _address,
            _country,
            _stateOfCountry,
            _city,
            _postalCode
        ) { address, country, state, city, postal ->
            LocationData(
                address = address,
                country = country,
                stateOfCountry = state,
                city = city,
                postalCode = postal
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = LocationData.Default
        )

    private val personalData: StateFlow<PersonalData> =
        combine(
            _username,
            _phone,
            _bio,
            _dob
        ) { username, phone, bio, dob ->
            PersonalData(
                username = username,
                phone = phone,
                bio = bio,
                dob = dob,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = PersonalData.Default,
        )


    private val profileData: StateFlow<ProfileData> =
        combine(
            personalData,
            locationData
        ) { personal, location ->
            ProfileData(
                personalData = personal,
                locationData = location,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = ProfileData(null, null),
        )


    val uiState: StateFlow<SettingsUiState> =
        combine(
            userDataRepository.userData,
            profileData,
            _hasUserProfileUpdated,
        ) { userData, profileData, hasUserProfileUpdated ->
            SettingsUiState.Success(
                data = SettingsData(
                    profileData = profileData,
                    userId = userData.userId,
                    hasUserProfileUpdated = hasUserProfileUpdated,
                    darkThemeSettings = DarkThemeConfigSettings(
                        brand = userData.themeBrand,
                        useDynamicColor = userData.useDynamicColor,
                        darkThemeConfig = userData.darkThemeConfig,
                    ),
                )
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(2000),
            initialValue = SettingsUiState.Loading
        )

    val observeMe: StateFlow<ClassifiUser?> =
        userDataRepository.userData.map {
            val id = it.userId
            userRepository.fetchUserById(id)
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = null,
            )

    fun updateTabIndexBasedOnSwipe() {
        _selectedTabIndex.value = when (isSwipeLeft) {
            false -> Math.floorMod(_selectedTabIndex.value!!.plus(1), tabs.size)
            true -> Math.floorMod(_selectedTabIndex.value!!.minus(1), tabs.size)
        }
    }

    fun updateTabIndex(i: Int) {
        _selectedTabIndex.value = i
    }

    fun updateCurrentSettingItemClicked(settingItemClicked: SettingItemClicked) {
        _currentSettingItemClicked.value = settingItemClicked
    }

    fun cancelSettingItemClicked() {
        _currentSettingItemClicked.value = SettingItemClicked.None
    }

    fun onUsernameChanged(username: String) {
        _username.value = username
    }

    fun resetUsername() {
        _username.value = ""
    }

    fun updateUsernameToDb(
        me: ClassifiUser?,
    ) = viewModelScope.launch {
        if(me != null) {
            me.account?.username = _username.value
            userRepository.updateUser(me)
        }
    }

    fun onPhoneChanged(phone: String) {
        _phone.value = phone
    }

    fun resetPhone() {
        _phone.value = ""
    }

    fun updatePhoneToDb(me:ClassifiUser?) = viewModelScope.launch {
        if(me != null) {
            when(me.profile) {
                null -> {
                    me.profile = UserProfile(
                        phone = _phone.value,
                    )
                }
                else -> {
                    me.profile?.phone = _phone.value
                }
            }
            userRepository.updateUser(me)
        }
    }

    fun onBioChanged(bio: String) {
        _bio.value = bio
    }

    fun resetBio() {
        _bio.value = ""
    }

    fun updateBioToDb() = viewModelScope.launch {

    }

    fun onDobChanged(dob: String) {
        _dob.value = dob
    }

    fun updateDobToDb() = viewModelScope.launch {

    }

    fun onAddressChanged(address: String) {
        _address.value = address
    }

    fun resetAddress() {
        _address.value = ""
    }

    fun updateAddressToDb() = viewModelScope.launch {

    }

    fun onCountryChanged(country: String) {
        _country.value = country
    }

    fun updateCountryToDb() = viewModelScope.launch {

    }

    fun onStateOfCountryChanged(state: String) {
        _stateOfCountry.value = state
    }

    fun resetStateOfCountry() {
        _stateOfCountry.value = ""
    }

    fun updateStateOfCountryToDb() = viewModelScope.launch {

    }

    fun onCityChanged(city: String) {
        _city.value = city
    }

    fun resetCity() {
        _city.value = ""
    }

    fun updateCityToDb() = viewModelScope.launch {

    }

    fun onPostalCodeChanged(code: String) {
        _postalCode.value = code
    }

    fun resetPostalCode() {
        _postalCode.value = ""
    }

    fun updatePostalToDb() = viewModelScope.launch {

    }

    fun onDarkThemeConfigDialogStateChange(state: Boolean) {
        _darkThemeConfigDialog.value = state
    }

    fun onDarkThemeChanged(theme: DarkThemeConfig) = viewModelScope.launch {
        userDataRepository.setDarkThemeConfig(theme)
    }

}

suspend fun UserRepository.updateUserProfile(userId: Long, profileData: ProfileData): Boolean {
    val currentUser = this.fetchUserById(userId) ?: return false
    profileData.let { data ->
        currentUser.let { user ->
            user.profile?.bio = data.personalData?.bio
            user.profile?.dob = data.personalData?.dob
            user.profile?.phone = data.personalData?.phone
            user.profile?.contact?.address = data.locationData?.address
            user.profile?.contact?.city = data.locationData?.city
            user.profile?.contact?.country = data.locationData?.country
            user.profile?.contact?.postalCode = data.locationData?.postalCode
            user.profile?.contact?.stateOfCountry = data.locationData?.stateOfCountry
        }
    }
    this.updateUser(currentUser)
    return true
}