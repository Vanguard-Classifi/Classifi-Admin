package com.vanguard.classifiadmin.onboarding.usecase

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import com.khalidtouch.chatme.domain.repository.UserDataRepository
import com.khalidtouch.classifiadmin.model.UserRole
import com.khalidtouch.classifiadmin.model.classifi.ClassifiUser
import com.khalidtouch.core.common.ClassifiDispatcher
import com.khalidtouch.core.common.Dispatcher
import com.khalidtouch.core.common.extensions.isEmailValid
import com.khalidtouch.core.common.extensions.isPasswordValid
import com.khalidtouch.core.common.extensions.orDefaultImageUrl
import com.khalidtouch.core.common.firestore.ClassifiStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject


class LoginUseCase @Inject constructor(
    @Dispatcher(ClassifiDispatcher.IO) private val ioDispatcher: CoroutineDispatcher
) {
    val TAG = "Login"
    private val authentication: FirebaseAuth = Firebase.auth
    val scope = CoroutineScope(ioDispatcher + SupervisorJob())

    operator fun invoke(loginData: LoginData, callback: (OnLoginState) -> Unit) {
        if (loginData.email.isBlank()) {
            return callback(OnLoginState.EmptyEmailOrPassword)
        }
        if (loginData.password.isBlank()) {
            return callback(OnLoginState.EmptyEmailOrPassword)
        }
        if (!loginData.email.isEmailValid()) {
            return callback(OnLoginState.InvalidCredentials)
        }
        if (!loginData.password.isPasswordValid()) {
            return callback(OnLoginState.InvalidCredentials)
        }

        try {
            authentication.signInWithEmailAndPassword(loginData.email, loginData.password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callback(OnLoginState.Success)
                    } else {
                        when (task.exception) {
                            is FirebaseAuthWeakPasswordException -> {
                                return@addOnCompleteListener callback(OnLoginState.WeakPassword)
                            }

                            is FirebaseAuthInvalidUserException -> {
                                return@addOnCompleteListener callback(OnLoginState.InvalidUser)
                            }

                            is FirebaseAuthInvalidCredentialsException -> {
                                return@addOnCompleteListener callback(OnLoginState.InvalidCredentials)
                            }

                            is FirebaseAuthUserCollisionException -> {
                                return@addOnCompleteListener callback(OnLoginState.UserAlreadyExists)
                            }

                            is FirebaseAuthEmailException -> {
                                return@addOnCompleteListener callback(OnLoginState.EmailNotFound)
                            }

                            is FirebaseNetworkException -> {
                                return@addOnCompleteListener callback(OnLoginState.NetworkProblem)
                            }

                            else -> {
                                Log.e(TAG, "invoke: something went wrong")
                            }
                        }
                        callback(OnLoginState.Failed)
                        return@addOnCompleteListener
                    }
                }
        } catch (e: Exception) {
            e.printStackTrace()
            Log.e(TAG, "invoke: exception is ${e.printStackTrace()}")
        }
    }

    private fun postAction(action: () -> Unit) = Handler(Looper.getMainLooper())
        .post { action() }
}


data class LoginData(
    val email: String,
    val password: String
)

sealed interface OnLoginState {
    object Success : OnLoginState
    object Failed : OnLoginState
    object EmptyEmailOrPassword : OnLoginState
    object InvalidCredentials : OnLoginState
    object InvalidUser : OnLoginState
    object UserAlreadyExists : OnLoginState
    object WeakPassword : OnLoginState
    object EmailNotFound : OnLoginState
    object NetworkProblem : OnLoginState
}