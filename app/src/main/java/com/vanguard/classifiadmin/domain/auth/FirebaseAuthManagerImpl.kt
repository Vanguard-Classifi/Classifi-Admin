package com.vanguard.classifiadmin.domain.auth

import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.vanguard.classifiadmin.domain.helpers.AuthExceptionState
import com.vanguard.classifiadmin.domain.helpers.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirebaseAuthManagerImpl @Inject constructor() : FirebaseAuthManager {
    private val auth = Firebase.auth

    override val currentUser: Resource<FirebaseUser?>
        get() {
            val user = try {
                auth.currentUser
            } catch (e: Exception) {
                return Resource.Error("Sorry an error occurred")
            }
            return Resource.Success(user)
        }


    override fun signUp(
        email: String?,
        password: String?,
        onResult: (Resource<FirebaseUser?>, Resource<AuthExceptionState?>) -> Unit
    ) {
        try {
            auth.createUserWithEmailAndPassword(email!!, password!!)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnCompleteListener { secondTask ->
                                onResult(
                                    Resource.Success(auth.currentUser),
                                    Resource.Success(null)
                                )
                            }
                        return@addOnCompleteListener
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            onResult(
                                Resource.Error("Error fetching user"),
                                Resource.Success(AuthExceptionState.UserAlreadyExists)
                            )
                            return@addOnCompleteListener
                        }

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            onResult(
                                Resource.Error("Error fetching user"),
                                Resource.Success(AuthExceptionState.InvalidUserCredentials)
                            )
                            return@addOnCompleteListener
                        }

                        if (task.exception is FirebaseAuthEmailException) {
                            onResult(
                                Resource.Error("Error fetching user"),
                                Resource.Success(AuthExceptionState.InvalidEmail)
                            )
                            return@addOnCompleteListener
                        }

                        if (task.exception is FirebaseNetworkException) {
                            onResult(
                                Resource.Error("Error fetching user"),
                                Resource.Success(AuthExceptionState.NetworkProblem)
                            )
                            return@addOnCompleteListener
                        } else {
                            onResult(
                                Resource.Error("Error parsing user"),
                                Resource.Success(AuthExceptionState.ErrorParsingUser)
                            )
                            return@addOnCompleteListener
                        }
                    }
                }
        } catch (e: Exception) {
            onResult(
                Resource.Error("Sorry something went wrong"),
                Resource.Error("Sorry something went wrong")
            )
        }
    }

    override fun signIn(
        email: String?,
        password: String?,
        onResult: (Resource<AuthExceptionState?>) -> Unit
    ) {
        try {
            auth.signInWithEmailAndPassword(email ?: "", password ?: "")
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        onResult(Resource.Success(null))
                        return@addOnCompleteListener
                    } else {
                        if (task.exception is FirebaseNetworkException) {
                            onResult(Resource.Success(AuthExceptionState.NetworkProblem))
                            return@addOnCompleteListener
                        }

                        if (task.exception is FirebaseAuthInvalidCredentialsException) {
                            onResult(Resource.Success(AuthExceptionState.InvalidUserCredentials))
                            return@addOnCompleteListener
                        }

                        if (task.exception is FirebaseAuthInvalidUserException) {
                            onResult(Resource.Success(AuthExceptionState.InvalidUser))
                            return@addOnCompleteListener
                        } else {
                            onResult(Resource.Success(AuthExceptionState.UserDoesNotExist))
                            return@addOnCompleteListener
                        }

                    }
                }
        } catch (e: Exception) {
            onResult(Resource.Error("Sorry something is wrong"))
        }
    }


    override fun signOut() {
        try {
            auth.signOut()
        } catch (_: Exception) {
        }
    }
}