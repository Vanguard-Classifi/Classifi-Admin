package com.khalidtouch.classifiadmin.settings.navigation.settings

import androidx.compose.foundation.gestures.DraggableState
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.khalidtouch.classifiadmin.data.util.ReadCountriesPagingSource
import com.khalidtouch.classifiadmin.settings.navigation.profile.LocationData
import com.khalidtouch.classifiadmin.settings.navigation.profile.PersonalData
import com.khalidtouch.classifiadmin.settings.navigation.profile.ProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    readCountriesPagingSource: ReadCountriesPagingSource,
) : ViewModel() {

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


    val uiState: StateFlow<SettingsUiState> = profileData.map {
        SettingsUiState.Success(
            data = SettingsData(
                profileData = it,
            )
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(2000),
        initialValue = SettingsUiState.Loading
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

    fun onPhoneChanged(phone: String) {
        _phone.value = phone
    }

    fun onBioChanged(bio: String) {
        _bio.value = bio
    }

    fun onDobChanged(dob: String) {
        _dob.value = dob
    }

    fun onAddressChanged(address: String) {
        _address.value = address
    }

    fun onCountryChanged(country: String) {
        _country.value = country
    }

    fun onStateOfCountryChanged(state: String) {
        _stateOfCountry.value = state
    }

    fun onCityChanged(city: String) {
        _city.value = city
    }

    fun onPostalCodeChanged(code: String) {
        _postalCode.value = code
    }
}