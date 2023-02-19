package com.vanguard.classifiadmin.data.repository

import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.domain.auth.FirebaseAuthManager
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainRepositoryImpl @Inject constructor(
    private val authManager: FirebaseAuthManager
): MainRepository {

    override val currentUser: Resource<FirebaseUser?>
        get() = authManager.currentUser

    override fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
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
}