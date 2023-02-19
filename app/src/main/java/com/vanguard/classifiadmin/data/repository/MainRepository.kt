package com.vanguard.classifiadmin.data.repository

import com.google.firebase.auth.FirebaseUser
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource

interface MainRepository {
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