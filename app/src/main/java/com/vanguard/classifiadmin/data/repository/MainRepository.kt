package com.vanguard.classifiadmin.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.network.models.ClassNetworkModel
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.SubjectNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource

interface MainRepository {
    val currentUser: Resource<FirebaseUser?>

    fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>,Resource<AuthExceptionState?>) -> Unit
    )

    fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    )

    fun signOut()

    //user
    suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun getUserByIdNetwork(userId: String, onResult: (Resource<UserNetworkModel?>) -> Unit)
    suspend fun getUserByEmailNetwork(email: String, onResult: (Resource<UserNetworkModel?>) -> Unit)
    suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit)


    //school
    suspend fun saveSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)
    suspend fun getSchoolByIdNetwork(schoolId: String, onResult: (Resource<SchoolNetworkModel?>) -> Unit)
    suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit)
    suspend fun deleteSchoolNetwork(school: SchoolNetworkModel, onResult: (Boolean) -> Unit)

    //avatar
    suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, String) -> Unit
    )

    suspend fun downloadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean, Long) -> Unit
    )


    //class
    suspend fun saveClassAsStagedNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveClassesAsStagedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )


    suspend fun saveClassAsVerifiedNetwork(myClass: ClassNetworkModel, onResult: (Boolean) -> Unit)

    suspend fun saveClassesAsVerifiedNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun getClassByIdNetwork(
        classId: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    )

    suspend fun getClassByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<ClassNetworkModel?>) -> Unit
    )

    suspend fun getVerifiedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    )

    suspend fun getStagedClassesNetwork(
        schoolId: String,
        onResult: (Resource<List<ClassNetworkModel>>) -> Unit
    )

    suspend fun deleteClassNetwork(
        myClass: ClassNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteClassesNetwork(
        myClasses: List<ClassNetworkModel>,
        onResult: (Boolean) -> Unit
    )


    //subject
    suspend fun saveSubjectAsStagedNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveSubjectsAsStagedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun saveSubjectAsVerifiedNetwork(subject: SubjectNetworkModel, onResult: (Boolean) -> Unit)

    suspend fun saveSubjectsAsVerifiedNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )

    suspend fun getSubjectByIdNetwork(
        subjectId: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    )

    suspend fun getSubjectByCodeNetwork(
        code: String,
        schoolId: String,
        onResult: (Resource<SubjectNetworkModel?>) -> Unit
    )

    suspend fun getVerifiedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )

    suspend fun getStagedSubjectsNetwork(
        schoolId: String,
        onResult: (Resource<List<SubjectNetworkModel>>) -> Unit
    )

    suspend fun deleteSubjectNetwork(
        subject: SubjectNetworkModel,
        onResult: (Boolean) -> Unit
    )

    suspend fun deleteSubjectsNetwork(
        subjects: List<SubjectNetworkModel>,
        onResult: (Boolean) -> Unit
    )
}