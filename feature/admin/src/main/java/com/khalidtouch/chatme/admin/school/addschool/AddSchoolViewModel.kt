package com.khalidtouch.chatme.admin.school.addschool

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.chatme.domain.usecases.OnRegisterSchoolState
import com.khalidtouch.chatme.domain.usecases.RegisterSchoolInfo
import com.khalidtouch.chatme.domain.usecases.RegisterSchoolUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddSchoolViewModel @Inject constructor(
    private val registerSchoolUseCase: RegisterSchoolUseCase,
    private val userDataRepository: UserDataRepository,
) : ViewModel() {
    private val _currentDestination =
        MutableStateFlow<AddSchoolDestination>(AddSchoolDestination.INPUT)
    private val _schoolName = MutableStateFlow<String>("")
    private val _schoolAddress = MutableStateFlow<String>("")

    val state: StateFlow<AddSchoolState> = combine(
        userDataRepository.userData,
        _currentDestination,
        _schoolName,
        _schoolAddress,
    ) { data, destination, name, address ->
        AddSchoolState(
            currentDestination = destination,
            schoolName = name,
            schoolAddress = address,
            myId = data.userId,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AddSchoolState.Default
    )

    fun onNavigate(to: AddSchoolDestination) {
        _currentDestination.value = to
    }

    fun clearCurrentDestination() {
        _currentDestination.value = AddSchoolDestination.INPUT
    }

    fun onSchoolNameChanged(name: String) {
        _schoolName.value = name
    }

    fun onSchoolAddressChanged(address: String) {
        _schoolAddress.value = address
    }

    fun onRegisterSchoolToDb(info: AddSchoolInfo, callback: (OnRegisterSchoolState) -> Unit) {
        try {
            viewModelScope.launch {
               registerSchoolUseCase(
                   info = RegisterSchoolInfo(
                       userId = info.userId,
                       schoolName = info.name,
                       schoolAddress = info.address,
                   ),
                   callback,
               )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

data class AddSchoolInfo(
    val userId: Long,
    val name: String,
    val address: String,
)

enum class AddSchoolDestination {
    INPUT, SUCCESS,
}

data class AddSchoolState(
    val myId: Long,
    val currentDestination: AddSchoolDestination,
    val schoolName: String,
    val schoolAddress: String,
) {
    companion object {
        val Default = AddSchoolState(
            currentDestination = AddSchoolDestination.INPUT,
            schoolName = "",
            schoolAddress = "",
            myId = -1L,
        )
    }
}