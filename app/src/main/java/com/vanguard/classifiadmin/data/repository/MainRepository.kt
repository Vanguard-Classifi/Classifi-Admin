package com.vanguard.classifiadmin.data.repository

import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
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
}