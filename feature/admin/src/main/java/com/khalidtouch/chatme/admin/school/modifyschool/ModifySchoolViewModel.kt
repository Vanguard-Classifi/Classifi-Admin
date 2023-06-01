package com.khalidtouch.chatme.admin.school.modifyschool

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ModifySchoolViewModel @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val schoolRepository: SchoolRepository,
) : ViewModel() {

    val TAG = "ModifySchool"
    val emptyUriString = "file://dev/null"

    private val _schoolName = MutableStateFlow<String>("")
    private val _schoolAddress = MutableStateFlow<String>("")
    private val _bannerImageUri = MutableStateFlow<Uri?>(null)
    private val _shouldUploadBannerImage = MutableStateFlow<Boolean>(false)

    val uiState: StateFlow<ModifySchoolUiState> = combine(
        _schoolName,
        _schoolAddress,
        _bannerImageUri,
        _shouldUploadBannerImage,
    ) { name, addr, banner, shouldUploadBannerImage ->
        ModifySchoolUiState(
            schoolName = name,
            schoolAddress = addr,
            bannerImageUri = banner,
            shouldUploadBannerImage = shouldUploadBannerImage,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ModifySchoolUiState.Default,
    )

    fun onSchoolNameChanged(name: String) {
        _schoolName.value = name
    }

    fun onSchoolAddressChanged(address: String) {
        _schoolAddress.value = address
    }

    fun onSchoolBannerImageChanged(banner: Uri?) {
        if(banner == null) return
        _bannerImageUri.value = banner
        _shouldUploadBannerImage.value = true
    }

    fun updateFieldsFromDb(school: ClassifiSchool?) {
        Log.e(TAG, "updateFieldsFromDb: has been called")
        _schoolName.value = school?.schoolName.orEmpty()
        _schoolAddress.value = school?.address.orEmpty()
        if(school?.bannerImage == null) return
        _bannerImageUri.value = Uri.parse(school.bannerImage?.orDefaultImageUrl())
    }

    fun onUpdateSchoolName(school: ClassifiSchool?) {
        try {
            if (school == null) return
            viewModelScope.launch {
                school.schoolName = _schoolName.value
                schoolRepository.updateSchool(school)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onUpdateSchoolAddress(school: ClassifiSchool?) {
        try {
            if (school == null) return
            viewModelScope.launch {
                school.address = _schoolAddress.value
                schoolRepository.updateSchool(school)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onUpdateSchoolBannerImage(school: ClassifiSchool?) {
        Log.e(TAG, "onUpdateSchoolBannerImage: called")
        try {
            if (school == null) return
            if(!_shouldUploadBannerImage.value) return
            viewModelScope.launch {
                school.bannerImage = _bannerImageUri.value.toString()
                schoolRepository.updateSchool(school)
                Log.e(TAG, "onUpdateSchoolBannerImage: new banner is ${_bannerImageUri.value.toString()}")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            _shouldUploadBannerImage.value = false
        }
    }
}


data class ModifySchoolUiState(
    val schoolName: String,
    val schoolAddress: String,
    val bannerImageUri: Uri?,
    val shouldUploadBannerImage: Boolean,
) {
    companion object {
        val Default = ModifySchoolUiState(
            schoolName = "",
            schoolAddress = "",
            bannerImageUri = null,
            shouldUploadBannerImage = false,
        )
    }
}