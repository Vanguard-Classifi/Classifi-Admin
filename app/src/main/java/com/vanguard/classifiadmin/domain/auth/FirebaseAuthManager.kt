package com.vanguard.classifiadmin.domain.auth

import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource

interface FirebaseAuthManager {
    val currentUser: Resource<FirebaseUser?>

    fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    )

    fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    )

    fun signOut()
}