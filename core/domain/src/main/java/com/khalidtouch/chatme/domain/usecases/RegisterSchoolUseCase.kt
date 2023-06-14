package com.khalidtouch.chatme.domain.usecases

import com.khalidtouch.chatme.domain.repository.SchoolRepository
import com.khalidtouch.classifiadmin.model.classifi.ClassifiSchool
import java.time.LocalDateTime
import javax.inject.Inject

class RegisterSchoolUseCase @Inject constructor(
    private val schoolRepository: SchoolRepository,
) {
    suspend operator fun invoke(
        info: RegisterSchoolInfo,
        callback: (OnRegisterSchoolState) -> Unit
    ) {
        callback(OnRegisterSchoolState.Starting)
        if (info.schoolName.isBlank()) {
            callback(OnRegisterSchoolState.NameOrAddressError)
            return
        }

        if (info.schoolAddress.isBlank()) {
            callback(OnRegisterSchoolState.NameOrAddressError)
            return
        }

        val schoolId: Long = System.currentTimeMillis() + info.schoolName.hashCode() + info.schoolAddress.hashCode()

        schoolRepository.saveSchool(
            ClassifiSchool(
                schoolId = schoolId,
                schoolName = info.schoolName,
                address = info.schoolAddress,
                dateCreated = LocalDateTime.now(),
            )
        )
        schoolRepository.registerUserWithSchool(
            userId = info.userId,
            schoolId = schoolId,
            schoolName = info.schoolName,
        )
        callback(OnRegisterSchoolState.Success)
    }
}

data class RegisterSchoolInfo(
    val userId: Long,
    val schoolName: String,
    val schoolAddress: String,
)

sealed interface OnRegisterSchoolState {
    object Starting : OnRegisterSchoolState
    object NameOrAddressError : OnRegisterSchoolState
    object Success : OnRegisterSchoolState
    object Failed : OnRegisterSchoolState
}