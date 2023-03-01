package com.vanguard.classifiadmin.data.repository

import android.net.Uri
import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.data.network.firestore.FirestoreManager
import com.vanguard.classifiadmin.data.network.models.SchoolNetworkModel
import com.vanguard.classifiadmin.data.network.models.UserNetworkModel
import com.vanguard.classifiadmin.data.network.storage.FirebaseStorage
import com.vanguard.classifiadmin.domain.auth.FirebaseAuthManager
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val authManager: FirebaseAuthManager,
    private val firestoreManager: FirestoreManager,
    private val firebaseStorage: FirebaseStorage,
) : MainRepository {

    override val currentUser: Resource<FirebaseUser?>
        get() = authManager.currentUser

    override fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>, Resource<AuthExceptionState?>) -> Unit
    ) {
        authManager.signUp(email, password, onResult)
    }

    override fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    ) {
        authManager.signIn(email, password, onResult)
    }

    override fun signOut() {
        authManager.signOut()
    }

    override suspend fun saveUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.saveUserNetwork(user, onResult)
    }

    override suspend fun getUserByIdNetwork(
        userId: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        firestoreManager.getUserByIdNetwork(userId, onResult)
    }

    override suspend fun getUserByEmailNetwork(
        email: String,
        onResult: (Resource<UserNetworkModel?>) -> Unit
    ) {
        firestoreManager.getUserByEmailNetwork(email, onResult)
    }

    override suspend fun deleteUserByIdNetwork(userId: String, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteUserByIdNetwork(userId, onResult)
    }

    override suspend fun deleteUserNetwork(user: UserNetworkModel, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteUserNetwork(user, onResult)
    }

    override suspend fun saveSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.saveSchoolNetwork(school, onResult)
    }

    override suspend fun getSchoolByIdNetwork(
        schoolId: String,
        onResult: (Resource<SchoolNetworkModel?>) -> Unit
    ) {
        firestoreManager.getSchoolByIdNetwork(schoolId, onResult)
    }

    override suspend fun deleteSchoolByIdNetwork(schoolId: String, onResult: (Boolean) -> Unit) {
        firestoreManager.deleteSchoolByIdNetwork(schoolId, onResult)
    }

    override suspend fun deleteSchoolNetwork(
        school: SchoolNetworkModel,
        onResult: (Boolean) -> Unit
    ) {
        firestoreManager.deleteSchoolNetwork(school, onResult)
    }

    override suspend fun uploadAvatar(
        fileUri: Uri,
        userId: String,
        onProgress: (Long, Long) -> Unit,
        onResult: (Boolean) -> Unit
    ) {
        firebaseStorage.uploadAvatar(fileUri, userId, onProgress, onResult)
    }
}